package character.classes.hunter.pet.abilities

import character.Ability
import character.Proc
import character.Resource
import character.classes.hunter.pet.HunterPet
import data.Constants
import mechanics.Melee
import sim.Event
import sim.EventResult
import sim.EventType
import sim.SimParticipant
import kotlin.random.Random

class GoreRank9 : Ability() {
    override val id: Int = 35298
    override val name: String = "Gore (Rank 9)"
    override fun gcdMs(sp: SimParticipant): Int = sp.physicalGcd().toInt()

    override fun resourceType(sp: SimParticipant): Resource.Type = Resource.Type.FOCUS
    override fun resourceCost(sp: SimParticipant): Double = 25.0

    // Pets have no gear, so each attack is modeled as an item with the same damage range
    val item = HunterPet.makePetAttackItem(name, 37.0, 61.0)
    override fun cast(sp: SimParticipant) {
        val goreMultiplier = if(Random.nextBoolean()) { 2.0 } else 1.0
        val damageRoll = Melee.baseDamageRoll(sp, item) * goreMultiplier
        val result = Melee.attackRoll(sp, damageRoll, item)

        // Save last hit state and fire event
        val event = Event(
            eventType = EventType.DAMAGE,
            damageType = Constants.DamageType.PHYSICAL,
            abilityName = name,
            amount = result.first,
            result = result.second,
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
