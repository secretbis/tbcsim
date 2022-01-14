package character.classes.warrior.abilities

import character.*
import character.classes.warrior.talents.FocusedRage
import character.classes.warrior.talents.ImprovedExecute
import data.Constants
import data.itemsets.OnslaughtBattlegear
import mechanics.Melee
import sim.Event
import sim.EventResult
import sim.EventType
import sim.SimParticipant

class ExecuteExtra : Execute() {
    override val name: String = "Execute (extra)"
}

open class Execute : Ability() {
    companion object {
        const val name = "Execute"
    }

    override val id: Int = 12292
    override val name: String = Companion.name
    override val icon: String = "inv_sword_48.jpg"

    override fun gcdMs(sp: SimParticipant): Int = sp.physicalGcd().toInt()

    override fun available(sp: SimParticipant): Boolean {
        return sp.sim.isExecutePhase() && super.available(sp)
    }

    override fun resourceType(sp: SimParticipant): Resource.Type = Resource.Type.RAGE
    override fun resourceCost(sp: SimParticipant): Double {
        val impExRanks = sp.character.klass.talents[ImprovedExecute.name]?.currentRank
        val impExDiscount = when(impExRanks) {
            1 -> 2
            2 -> 5
            else -> 0
        }
        val focusedRageRanks = sp.character.klass.talents[FocusedRage.name]?.currentRank ?: 0

        // Check T6 set bonus
        val t6Bonus = sp.buffs[OnslaughtBattlegear.TWO_SET_BUFF_NAME] != null
        val t6Discount = if(t6Bonus) { OnslaughtBattlegear.twoSetExecuteCostReduction() } else 0.0

        return 15.0 - impExDiscount - t6Discount - focusedRageRanks
    }

    override fun cast(sp: SimParticipant) {
        val item = sp.character.gear.mainHand
        val res = sp.resources[resourceType(sp)]!!
        val damage = 925.0 + res.currentAmount * 21
        val result = Melee.attackRoll(sp, damage, item, isWhiteDmg = false)

        // Drain rage
        sp.subtractResource(res.currentAmount, Resource.Type.RAGE, ExecuteExtra())

        // Save last hit state and fire event
        val event = Event(
            eventType = EventType.DAMAGE,
            damageType = Constants.DamageType.PHYSICAL,
            ability = this,
            amount = result.first,
            result = result.second,
            abilityThreatMultiplier = 1.25
        )
        sp.logEvent(event)

        // Proc anything that can proc off a yellow hit
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
}
