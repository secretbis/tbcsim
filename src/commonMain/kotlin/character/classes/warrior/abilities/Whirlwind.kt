package character.classes.warrior.abilities

import character.*
import character.classes.warrior.talents.ImprovedWhirlwind
import data.Constants
import data.model.Item
import mechanics.Melee
import sim.Event
import sim.SimIteration

class Whirlwind : Ability() {
    companion object {
        const val name = "Whirlwind"
    }

    override val id: Int = 8989
    override val name: String = Companion.name

    override fun cooldownMs(sim: SimIteration): Int {
        val impWWRanks = sim.subject.klass.talents[ImprovedWhirlwind.name]?.currentRank ?: 0
        val discount = 1000 * impWWRanks
        return 6000 - discount
    }

    override fun resourceType(sim: SimIteration): Resource.Type = Resource.Type.RAGE
    override fun resourceCost(sim: SimIteration): Double = 30.0

    override fun cast(sim: SimIteration) {
        val mh = sim.subject.gear.mainHand
        val mhDamageRoll = Melee.baseDamageRoll(sim, mh, isNormalized = true)
        val mhResult = Melee.attackRoll(sim, mhDamageRoll, mh)

        // Save last hit state and fire event
        val mhEvent = Event(
            eventType = Event.Type.DAMAGE,
            damageType = Constants.DamageType.PHYSICAL,
            abilityName = "$name (MH)",
            amount = mhResult.first,
            result = mhResult.second,
        )
        sim.logEvent(mhEvent)

        fireTriggers(sim, mh, mhEvent, mhResult)

        if(sim.isDualWielding()) {
            val oh = sim.subject.gear.offHand
            val ohDamageRoll = Melee.baseDamageRoll(sim, oh, isNormalized = true)
            val ohResult = Melee.attackRoll(sim, ohDamageRoll, oh)

            // Save last hit state and fire event
            val ohEvent = Event(
                eventType = Event.Type.DAMAGE,
                damageType = Constants.DamageType.PHYSICAL,
                abilityName = "$name (OH)",
                amount = ohResult.first,
                result = ohResult.second,
            )
            sim.logEvent(ohEvent)

            fireTriggers(sim, oh, ohEvent, ohResult)
        }
    }

    private fun fireTriggers(sim: SimIteration, item: Item, event: Event, result: Pair<Double, Event.Result>) {
        val triggerTypes = when(result.second) {
            Event.Result.HIT -> listOf(Proc.Trigger.MELEE_YELLOW_HIT, Proc.Trigger.PHYSICAL_DAMAGE)
            Event.Result.CRIT -> listOf(Proc.Trigger.MELEE_YELLOW_CRIT, Proc.Trigger.PHYSICAL_DAMAGE)
            Event.Result.MISS -> listOf(Proc.Trigger.MELEE_MISS)
            Event.Result.DODGE -> listOf(Proc.Trigger.MELEE_DODGE)
            Event.Result.PARRY -> listOf(Proc.Trigger.MELEE_PARRY)
            Event.Result.BLOCK -> listOf(Proc.Trigger.MELEE_YELLOW_HIT, Proc.Trigger.PHYSICAL_DAMAGE)
            Event.Result.BLOCKED_CRIT -> listOf(Proc.Trigger.MELEE_YELLOW_CRIT, Proc.Trigger.PHYSICAL_DAMAGE)
            else -> null
        }

        if(triggerTypes != null) {
            sim.fireProc(triggerTypes, listOf(item), this, event)
        }
    }

    override fun gcdMs(sim: SimIteration): Int = sim.physicalGcd().toInt()
}
