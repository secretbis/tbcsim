package character.classes.rogue.abilities

import character.*
import mechanics.Melee
import sim.Event
import sim.SimParticipant
import data.Constants
import character.classes.rogue.talents.*

class SinisterStrike : Ability() {
    companion object {
        const val name = "Sinister Strike"
    }

    override val id: Int = 26862
    override val name: String = Companion.name

    override fun gcdMs(sp: SimParticipant): Int = sp.physicalGcd().toInt()

    override fun resourceType(sp: SimParticipant): Resource.Type = Resource.Type.ENERGY
    override fun resourceCost(sp: SimParticipant): Double {
        val improved = sp.character.klass.talents[ImprovedSinisterStrike.name] as ImprovedSinisterStrike?
        val reduction = improved?.reducedEnergy() ?: 0.0
        return 45.0 - reduction
    }

    val bonusDamage = 98.0
    override fun cast(sp: SimParticipant) {
        val lethality = sp.character.klass.talents[Lethality.name] as Lethality?
        val critDmgMultiplier = lethality?.critDamageMultiplier() ?: 1.0

        var increasedDamagePercent = 0.0
        
        val aggression = sp.character.klass.talents[Aggression.name] as ImprovedEviscerate?
        increasedDamagePercent += aggression?.damageIncreasePercent() ?: 0.0
        val surprise = sp.character.klass.talents[SurpriseAttacks.name] as SurpriseAttacks?
        increasedDamagePercent += surprise?.damageIncreasePercent() ?: 0.0
        
        val dmgMultiplier = 1 + (increasedDamagePercent / 100.0).coerceAtLeast(0.0)


        // unsure if dmg calculation is correct with the multiplier
        val item = sp.character.gear.mainHand
        val damageRoll = (Melee.baseDamageRoll(sp, item, isNormalized = true) + bonusDamage) * dmgMultiplier
        val result = Melee.attackRoll(sp, damageRoll, item, isWhiteDmg = false, abilityAdditionalCritDamageMultiplier = critDmgMultiplier)
        
        if(result.second != Event.Result.MISS && result.second != Event.Result.DODGE) {
            sp.addResource(1, Resource.Type.COMBO_POINT, name)
        }

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
