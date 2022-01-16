package character.classes.warrior.abilities

import character.*
import character.classes.warrior.talents.FocusedRage
import character.classes.warrior.talents.ImprovedWhirlwind
import data.Constants
import data.itemsets.WarbringerBattlegear
import data.model.Item
import mechanics.Melee
import sim.*

class WhirlwindOH : Whirlwind() {
    override val name: String = "Whirlwind (OH)"
}

open class Whirlwind : Ability() {
    companion object {
        const val name = "Whirlwind"
    }

    override val id: Int = 8989
    override val name: String = Companion.name
    override val icon: String = "ability_whirlwind.jpg"

    override fun cooldownMs(sp: SimParticipant): Int {
        val impWWRanks = sp.character.klass.talents[ImprovedWhirlwind.name]?.currentRank ?: 0
        val discount = 1000 * impWWRanks
        return 10000 - discount
    }

    override fun resourceType(sp: SimParticipant): Resource.Type = Resource.Type.RAGE
    override fun resourceCost(sp: SimParticipant): Double {
        val baseCost = 25.0

        val focusedRageRanks = sp.character.klass.talents[FocusedRage.name]?.currentRank ?: 0

        // Check T4 set bonus
        val t4Bonus = sp.buffs[WarbringerBattlegear.TWO_SET_BUFF_NAME] != null
        val t4CostReduction = if(t4Bonus) { WarbringerBattlegear.twoSetWhirlwindCostReduction() } else 0.0

        return baseCost - t4CostReduction - focusedRageRanks
    }

    override fun available(sp: SimParticipant): Boolean {
        val isBerserkerStance = sp.buffs[BerserkerStance.name] != null
        return isBerserkerStance && super.available(sp)
    }

    override fun cast(sp: SimParticipant) {
        val mh = sp.character.gear.mainHand
        val mhDamageRoll = Melee.baseDamageRoll(sp, mh, isNormalized = true)
        val mhResult = Melee.attackRoll(sp, mhDamageRoll, mh)

        // Save last hit state and fire event
        val mhEvent = Event(
            eventType = EventType.DAMAGE,
            damageType = Constants.DamageType.PHYSICAL,
            ability = this,
            amount = mhResult.first,
            result = mhResult.second,
            abilityThreatMultiplier = 1.25
        )
        sp.logEvent(mhEvent)

        fireTriggers(sp, mh, mhEvent, mhResult)

        if(sp.isDualWielding()) {
            val oh = sp.character.gear.offHand
            val ohDamageRoll = Melee.baseDamageRoll(sp, oh, isNormalized = true)
            val ohResult = Melee.attackRoll(sp, ohDamageRoll, oh)

            // Save last hit state and fire event
            val ohEvent = Event(
                eventType = EventType.DAMAGE,
                damageType = Constants.DamageType.PHYSICAL,
                ability = WhirlwindOH(),
                amount = ohResult.first,
                result = ohResult.second,
                abilityThreatMultiplier = 1.25
            )
            sp.logEvent(ohEvent)

            fireTriggers(sp, oh, ohEvent, ohResult)
        }
    }

    private fun fireTriggers(sp: SimParticipant, item: Item, event: Event, result: Pair<Double, EventResult>) {
        val triggerTypes = when(result.second) {
            EventResult.HIT -> listOf(Proc.Trigger.MELEE_YELLOW_HIT, Proc.Trigger.PHYSICAL_DAMAGE)
            EventResult.CRIT -> listOf(Proc.Trigger.MELEE_YELLOW_CRIT, Proc.Trigger.PHYSICAL_DAMAGE)
            EventResult.MISS -> listOf(Proc.Trigger.MELEE_MISS)
            EventResult.DODGE -> listOf(Proc.Trigger.MELEE_DODGE)
            EventResult.PARRY -> listOf(Proc.Trigger.MELEE_PARRY)
            EventResult.BLOCK -> listOf(Proc.Trigger.MELEE_YELLOW_HIT, Proc.Trigger.PHYSICAL_DAMAGE)
            EventResult.BLOCKED_CRIT -> listOf(Proc.Trigger.MELEE_YELLOW_CRIT, Proc.Trigger.PHYSICAL_DAMAGE)
            else -> null
        }

        if(triggerTypes != null) {
            sp.fireProc(triggerTypes, listOf(item), this, event)
        }
    }

    override fun gcdMs(sp: SimParticipant): Int = sp.physicalGcd().toInt()
}
