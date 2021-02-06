package character.classes.shaman.abilities

import character.Ability
import character.Buff
import character.Proc
import character.Stats
import character.classes.shaman.talents.Stormstrike as StormstrikeTalent
import data.Constants
import data.model.Item
import mechanics.Melee
import sim.Event
import sim.SimIteration

class Stormstrike : Ability() {
    companion object {
        const val name = "Stormstrike"
    }

    override val id: Int = 17364
    override val name: String = Companion.name
    override fun cooldownMs(sim: SimIteration): Int = 10000
    override fun gcdMs(sim: SimIteration): Int = sim.physicalGcd().toInt()

    override fun resourceCost(sim: SimIteration): Double {
        return 0.08 * sim.subject.klass.baseMana
    }

    override fun available(sim: SimIteration): Boolean {
        return sim.subject.klass.talents[StormstrikeTalent.name]?.currentRank == 1 && super.available(sim)
    }

    val proc = fun(buff: Buff): Proc {
        return object : Proc() {
            override val triggers: List<Trigger> = listOf(
                Trigger.NATURE_DAMAGE
            )
            override val type: Type = Type.STATIC

            override fun proc(sim: SimIteration, items: List<Item>?, ability: Ability?, event: Event?) {
                sim.consumeBuff(buff)
            }
        }
    }

    val buff = object : Buff() {
        override val name: String = "${Companion.name} (Nature)"
        override val durationMs: Int = 12000
        override val maxCharges: Int = 2

        // Increase nature damage for as long as we have charges
        override fun modifyStats(sim: SimIteration): Stats {
            return Stats(natureDamageMultiplier = 1.2)
        }

        val proc = proc(this)

        // Proc off of nature damage to reduce our stacks
        override fun procs(sim: SimIteration): List<Proc> = listOf(proc)
    }

    override fun cast(sim: SimIteration) {
        // Do attacks
        // Stormstrike is yellow, and not normalized per EJ
        val mhItem = sim.subject.gear.mainHand
        val mhAttack = Melee.baseDamageRoll(sim, mhItem, isNormalized = false)
        val mhResult = Melee.attackRoll(sim, mhAttack, mhItem, isWhiteDmg = false)

        val ohItem = sim.subject.gear.offHand
        val ohAttack = Melee.baseDamageRoll(sim, ohItem, isNormalized = false)
        val ohResult = Melee.attackRoll(sim, ohAttack, ohItem, isWhiteDmg = false)

        // TODO: Distinguish MH and OH events in the logs?
        val eventMh = Event(
            eventType = Event.Type.DAMAGE,
            damageType = Constants.DamageType.PHYSICAL,
            abilityName = name,
            amount = mhResult.first,
            result = mhResult.second,
        )
        sim.logEvent(eventMh)

        val eventOh = Event(
            eventType = Event.Type.DAMAGE,
            damageType = Constants.DamageType.PHYSICAL,
            abilityName = name,
            amount = ohResult.first,
            result = ohResult.second,
        )
        sim.logEvent(eventOh)

        // Apply the nature buff
        sim.addBuff(buff)

        // Proc anything that can proc off a yellow hit
        // TODO: Should I fire procs off miss/dodge/parry/etc?
        //       Would need to create new events to distinguish yellow-mitigation, to avoid consuming things like Flurry charges
        val triggerTypes = when(mhResult.second) {
            Event.Result.HIT -> listOf(Proc.Trigger.MELEE_YELLOW_HIT, Proc.Trigger.PHYSICAL_DAMAGE)
            Event.Result.CRIT -> listOf(Proc.Trigger.MELEE_YELLOW_CRIT, Proc.Trigger.PHYSICAL_DAMAGE)
            else -> null
        }

        if(triggerTypes != null) {
            sim.fireProc(triggerTypes, listOf(mhItem), this, eventMh)
        }

        // TODO: This is modeled as two distint hit events for the purposes of procs
        //       Confirm if that is correct behavior
        val triggerTypesOh = when(ohResult.second) {
            Event.Result.HIT -> listOf(Proc.Trigger.MELEE_YELLOW_HIT, Proc.Trigger.PHYSICAL_DAMAGE)
            Event.Result.CRIT -> listOf(Proc.Trigger.MELEE_YELLOW_CRIT, Proc.Trigger.PHYSICAL_DAMAGE)
            else -> null
        }

        if(triggerTypesOh != null) {
            sim.fireProc(triggerTypesOh, listOf(ohItem), this, eventOh)
        }
    }
}
