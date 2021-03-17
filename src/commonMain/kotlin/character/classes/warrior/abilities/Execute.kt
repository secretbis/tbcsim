package character.classes.warrior.abilities

import character.*
import character.classes.warrior.talents.ImprovedExecute
import data.Constants
import mechanics.Melee
import sim.Event
import sim.SimParticipant

class Execute : Ability() {
    companion object {
        const val name = "Execute"
    }

    override val id: Int = 12292
    override val name: String = Companion.name

    override fun gcdMs(sp: SimParticipant): Int = sp.physicalGcd().toInt()

    override fun available(sp: SimParticipant): Boolean {
        return sp.sim.isExecutePhase() && super.available(sp)
    }

    override fun resourceType(sp: SimParticipant): Resource.Type = Resource.Type.RAGE
    override fun resourceCost(sp: SimParticipant): Double {
        val impExRanks = sp.character.klass.talents[ImprovedExecute.name]?.currentRank
        val discount = when(impExRanks) {
            1 -> 2
            2 -> 5
            else -> 0
        }
        return 15.0 - discount
    }

    override fun cast(sp: SimParticipant) {
        val item = sp.character.gear.mainHand
        val damage = 925.0 + sp.resource.currentAmount * 21
        val result = Melee.attackRoll(sp, damage, item, isWhiteDmg = false)

        // Drain rage
        sp.subtractResource(sp.resource.currentAmount, Resource.Type.RAGE, "Execute (extra)")

        // Save last hit state and fire event
        val event = Event(
            eventType = Event.Type.DAMAGE,
            damageType = Constants.DamageType.PHYSICAL,
            abilityName = name,
            amount = result.first,
            result = result.second,
        )
        sp.logEvent(event)

        // Proc anything that can proc off a yellow hit
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
}
