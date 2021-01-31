package character.classes.shaman.abilities

import character.Ability
import character.Buff
import character.Proc
import character.Stats
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

    override fun resourceCost(sim: SimIteration): Double {
        return 0.08 * sim.subject.klass.baseMana
    }

    val proc = fun(buff: Buff): Proc {
        return object : Proc() {
            override val triggers: List<Trigger> = listOf(
                Trigger.NATURE_DAMAGE
            )
            override val type: Type = Type.STATIC

            override fun proc(sim: SimIteration, items: List<Item>?, ability: Ability?) {
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

    override fun cast(sim: SimIteration, free: Boolean) {
        // Do attacks
        // Stormstrike is yellow, and not normalized per EJ
        val mhItem = sim.subject.gear.mainHand
        val mhAttack = Melee.baseDamageRoll(sim, mhItem, isNormalized = false)
        val mhResult = Melee.attackRoll(sim, mhAttack, isWhiteDmg = false)

        val ohItem = sim.subject.gear.offHand
        val ohAttack = Melee.baseDamageRoll(sim, ohItem, isNormalized = false)
        val ohResult = Melee.attackRoll(sim, ohAttack, isWhiteDmg = false, isOffHand = true)

        // TODO: Distinguish MH and OH events in the logs?
        sim.logEvent(Event(
            eventType = Event.Type.DAMAGE,
            damageType = Constants.DamageType.PHYSICAL,
            abilityName = name,
            amount = mhResult.first,
            result = mhResult.second,
        ))

        sim.logEvent(Event(
            eventType = Event.Type.DAMAGE,
            damageType = Constants.DamageType.PHYSICAL,
            abilityName = name,
            amount = ohResult.first,
            result = ohResult.second,
        ))

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
            sim.fireProc(triggerTypes, listOf(mhItem), this)
        }

        // TODO: This is modeled as two distint hit events for the purposes of procs
        //       Confirm if that is correct behavior
        val triggerTypesOh = when(ohResult.second) {
            Event.Result.HIT -> listOf(Proc.Trigger.MELEE_YELLOW_HIT, Proc.Trigger.PHYSICAL_DAMAGE)
            Event.Result.CRIT -> listOf(Proc.Trigger.MELEE_YELLOW_CRIT, Proc.Trigger.PHYSICAL_DAMAGE)
            else -> null
        }

        if(triggerTypesOh != null) {
            sim.fireProc(triggerTypesOh, listOf(ohItem), this)
        }
    }

    override val baseCastTimeMs: Int = 0
    override fun gcdMs(sim: SimIteration): Int = sim.physicalGcd().toInt()
}
