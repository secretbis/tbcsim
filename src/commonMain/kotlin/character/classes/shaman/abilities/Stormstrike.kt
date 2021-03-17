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
import sim.SimParticipant

class Stormstrike : Ability() {
    companion object {
        const val name = "Stormstrike"
    }

    override val id: Int = 17364
    override val name: String = Companion.name
    override fun cooldownMs(sp: SimParticipant): Int = 10000
    override fun gcdMs(sp: SimParticipant): Int = sp.physicalGcd().toInt()

    override fun resourceCost(sp: SimParticipant): Double {
        return 0.08 * sp.character.klass.baseMana
    }

    override fun available(sp: SimParticipant): Boolean {
        return sp.character.klass.talents[StormstrikeTalent.name]?.currentRank == 1 && super.available(sp)
    }

    val proc = fun(buff: Buff): Proc {
        return object : Proc() {
            override val triggers: List<Trigger> = listOf(
                Trigger.NATURE_DAMAGE
            )
            override val type: Type = Type.STATIC

            override fun proc(sp: SimParticipant, items: List<Item>?, ability: Ability?, event: Event?) {
                sp.consumeBuff(buff)
            }
        }
    }

    val buff = object : Buff() {
        override val name: String = "Stormstrike (Nature)"
        override val durationMs: Int = 12000
        override val maxCharges: Int = 2

        // Increase nature damage for as long as we have charges
        override fun modifyStats(sp: SimParticipant): Stats {
            return Stats(natureDamageMultiplier = 1.2)
        }

        val proc = proc(this)

        // Proc off of nature damage to reduce our stacks
        override fun procs(sp: SimParticipant): List<Proc> = listOf(proc)
    }

    override fun cast(sp: SimParticipant) {
        // Do attacks
        // Stormstrike is yellow, and not normalized per EJ
        val mhItem = sp.character.gear.mainHand
        val mhAttack = Melee.baseDamageRoll(sp, mhItem, isNormalized = false)
        val mhResult = Melee.attackRoll(sp, mhAttack, mhItem, isWhiteDmg = false)

        val ohItem = sp.character.gear.offHand
        val ohAttack = Melee.baseDamageRoll(sp, ohItem, isNormalized = false)
        val ohResult = Melee.attackRoll(sp, ohAttack, ohItem, isWhiteDmg = false)

        // TODO: Distinguish MH and OH events in the logs?
        val eventMh = Event(
            eventType = Event.Type.DAMAGE,
            damageType = Constants.DamageType.PHYSICAL,
            abilityName = "$name (MH)",
            amount = mhResult.first,
            result = mhResult.second,
        )
        sp.logEvent(eventMh)

        val eventOh = Event(
            eventType = Event.Type.DAMAGE,
            damageType = Constants.DamageType.PHYSICAL,
            abilityName = "$name (OH)",
            amount = ohResult.first,
            result = ohResult.second,
        )
        sp.logEvent(eventOh)

        // Apply the nature buff
        sp.addBuff(buff)

        // Proc anything that can proc off a yellow hit
        // TODO: Should I fire procs off miss/dodge/parry/etc?
        //       Would need to create new events to distinguish yellow-mitigation, to avoid consuming things like Flurry charges
        val triggerTypes = when(mhResult.second) {
            Event.Result.HIT -> listOf(Proc.Trigger.MELEE_YELLOW_HIT, Proc.Trigger.PHYSICAL_DAMAGE)
            Event.Result.CRIT -> listOf(Proc.Trigger.MELEE_YELLOW_CRIT, Proc.Trigger.PHYSICAL_DAMAGE)
            else -> null
        }

        if(triggerTypes != null) {
            sp.fireProc(triggerTypes, listOf(mhItem), this, eventMh)
        }

        // TODO: This is modeled as two distint hit events for the purposes of procs
        //       Confirm if that is correct behavior
        val triggerTypesOh = when(ohResult.second) {
            Event.Result.HIT -> listOf(Proc.Trigger.MELEE_YELLOW_HIT, Proc.Trigger.PHYSICAL_DAMAGE)
            Event.Result.CRIT -> listOf(Proc.Trigger.MELEE_YELLOW_CRIT, Proc.Trigger.PHYSICAL_DAMAGE)
            else -> null
        }

        if(triggerTypesOh != null) {
            sp.fireProc(triggerTypesOh, listOf(ohItem), this, eventOh)
        }
    }
}
