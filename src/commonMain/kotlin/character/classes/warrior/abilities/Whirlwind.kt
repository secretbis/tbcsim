package character.classes.warrior.abilities

import character.*
import character.classes.warrior.talents.ImprovedWhirlwind
import data.Constants
import data.itemsets.WarbringerBattlegear
import data.model.Item
import mechanics.Melee
import sim.Event
import sim.SimIteration
import sim.SimParticipant

class Whirlwind : Ability() {
    companion object {
        const val name = "Whirlwind"
    }

    override val id: Int = 8989
    override val name: String = Companion.name

    override fun cooldownMs(sp: SimParticipant): Int {
        val impWWRanks = sp.character.klass.talents[ImprovedWhirlwind.name]?.currentRank ?: 0
        val discount = 1000 * impWWRanks
        return 10000 - discount
    }

    override fun resourceType(sp: SimParticipant): Resource.Type = Resource.Type.RAGE
    override fun resourceCost(sp: SimParticipant): Double {
        val baseCost = 25.0

        // Check T4 set bonus
        val t4Bonus = sp.buffs[WarbringerBattlegear.TWO_SET_BUFF_NAME] != null
        val t4CostReduction = if(t4Bonus) { WarbringerBattlegear.twoSetWhirlwindCostReduction() } else 0.0

        return baseCost - t4CostReduction
    }

    override fun cast(sp: SimParticipant) {
        val mh = sp.character.gear.mainHand
        val mhDamageRoll = Melee.baseDamageRoll(sp, mh, isNormalized = true)
        val mhResult = Melee.attackRoll(sp, mhDamageRoll, mh)

        // Save last hit state and fire event
        val mhEvent = Event(
            eventType = Event.Type.DAMAGE,
            damageType = Constants.DamageType.PHYSICAL,
            abilityName = "$name (MH)",
            amount = mhResult.first,
            result = mhResult.second,
        )
        sp.logEvent(mhEvent)

        fireTriggers(sp, mh, mhEvent, mhResult)

        if(sp.isDualWielding()) {
            val oh = sp.character.gear.offHand
            val ohDamageRoll = Melee.baseDamageRoll(sp, oh, isNormalized = true)
            val ohResult = Melee.attackRoll(sp, ohDamageRoll, oh)

            // Save last hit state and fire event
            val ohEvent = Event(
                eventType = Event.Type.DAMAGE,
                damageType = Constants.DamageType.PHYSICAL,
                abilityName = "$name (OH)",
                amount = ohResult.first,
                result = ohResult.second,
            )
            sp.logEvent(ohEvent)

            fireTriggers(sp, oh, ohEvent, ohResult)
        }
    }

    private fun fireTriggers(sp: SimParticipant, item: Item, event: Event, result: Pair<Double, Event.Result>) {
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
            sp.fireProc(triggerTypes, listOf(item), this, event)
        }
    }

    override fun gcdMs(sp: SimParticipant): Int = sp.physicalGcd().toInt()
}
