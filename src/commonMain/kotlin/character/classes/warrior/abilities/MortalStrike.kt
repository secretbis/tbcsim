package character.classes.warrior.abilities

import character.*
import character.classes.warrior.talents.ImprovedMortalStrike
import character.classes.warrior.talents.MortalStrike as MortalStrikeTalent
import data.Constants
import mechanics.Melee
import sim.Event
import sim.SimParticipant

class MortalStrike : Ability() {
    companion object {
        const val name = "Mortal Strike"
    }

    override val id: Int = 30330
    override val name: String = Companion.name

    override fun gcdMs(sp: SimParticipant): Int = sp.physicalGcd().toInt()

    override fun cooldownMs(sp: SimParticipant): Int {
        val impMSRanks = sp.character.klass.talents[ImprovedMortalStrike.name]?.currentRank ?: 0
        val discount = 200 * impMSRanks
        return 6000 - discount
    }

    override fun resourceType(sp: SimParticipant): Resource.Type = Resource.Type.RAGE
    override fun resourceCost(sp: SimParticipant): Double = 30.0

    override fun available(sp: SimParticipant): Boolean {
        return sp.character.klass.talents[MortalStrikeTalent.name]?.currentRank == 1 && super.available(sp)
    }

    override fun cast(sp: SimParticipant) {
        val impMSRanks = sp.character.klass.talents[ImprovedMortalStrike.name]?.currentRank ?: 0
        val dmgMult = 1.0 + (0.01 * impMSRanks)

        val item = sp.character.gear.mainHand
        val damageRoll = Melee.baseDamageRoll(sp, item, isNormalized = true) * dmgMult
        val result = Melee.attackRoll(sp, damageRoll, item, isWhiteDmg = false)

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