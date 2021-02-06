package character.classes.warrior.abilities

import character.*
import character.classes.warrior.talents.ImprovedMortalStrike
import character.classes.warrior.talents.MortalStrike as MortalStrikeTalent
import data.Constants
import mechanics.Melee
import sim.Event
import sim.SimIteration

class MortalStrike : Ability() {
    companion object {
        const val name = "Mortal Strike"
    }

    override val id: Int = 30330
    override val name: String = Companion.name

    override fun gcdMs(sim: SimIteration): Int = sim.physicalGcd().toInt()

    override fun cooldownMs(sim: SimIteration): Int {
        val impMSRanks = sim.subject.klass.talents[ImprovedMortalStrike.name]?.currentRank ?: 0
        val discount = 200 * impMSRanks
        return 6000 - discount
    }

    override fun resourceType(sim: SimIteration): Resource.Type = Resource.Type.RAGE
    override fun resourceCost(sim: SimIteration): Double = 30.0

    override fun available(sim: SimIteration): Boolean {
        return sim.subject.klass.talents[MortalStrikeTalent.name]?.currentRank == 1 && super.available(sim)
    }

    override fun cast(sim: SimIteration) {
        val impMSRanks = sim.subject.klass.talents[ImprovedMortalStrike.name]?.currentRank ?: 0
        val dmgMult = 1.0 + (0.01 * impMSRanks)

        val item = sim.subject.gear.mainHand
        val damageRoll = Melee.baseDamageRoll(sim, item, isNormalized = true) * dmgMult
        val result = Melee.attackRoll(sim, damageRoll, item, isWhiteDmg = false)

        // Save last hit state and fire event
        val event = Event(
            eventType = Event.Type.DAMAGE,
            damageType = Constants.DamageType.PHYSICAL,
            abilityName = name,
            amount = result.first,
            result = result.second,
        )
        sim.logEvent(event)

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
            sim.fireProc(triggerTypes, listOf(item), this, event)
        }
    }
}
