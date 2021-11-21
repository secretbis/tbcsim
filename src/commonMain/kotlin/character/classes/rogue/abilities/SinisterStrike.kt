package character.classes.rogue.abilities

import character.*
import mechanics.Melee
import sim.Event
import sim.SimParticipant
import data.Constants
import character.classes.rogue.talents.*
import data.itemsets.SlayersArmor
import sim.EventResult
import sim.EventType

class SinisterStrike : Ability() {
    companion object {
        const val name = "Sinister Strike"
    }

    override val id: Int = 26862
    override val name: String = Companion.name
    override val icon: String = "spell_shadow_ritualofsacrifice.jpg"

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

        val aggression = sp.character.klass.talents[Aggression.name] as Aggression?
        increasedDamagePercent += aggression?.damageIncreasePercent() ?: 0.0
        val surprise = sp.character.klass.talents[SurpriseAttacks.name] as SurpriseAttacks?
        increasedDamagePercent += surprise?.damageIncreasePercent() ?: 0.0
        val slayers = sp.buffs[SlayersArmor.FOUR_SET_BUFF_NAME]
        increasedDamagePercent += if (slayers != null) { SlayersArmor.fourSetGeneratorDamageIncreasePercent() } else 0.0

        val dmgMultiplier = 1 + (increasedDamagePercent / 100.0).coerceAtLeast(0.0)


        // TODO:
        // unsure if dmg calculation is correct with the multiplier
        // from testing this seems to do slightly too much dmg.
        val item = sp.character.gear.mainHand
        val damageRoll = (Melee.baseDamageRoll(sp, item, isNormalized = true) + bonusDamage) * dmgMultiplier
        val result = Melee.attackRoll(sp, damageRoll, item, isWhiteDmg = false, abilityAdditionalCritDamageMultiplier = critDmgMultiplier)

        if(result.second != EventResult.MISS && result.second != EventResult.DODGE && result.second != EventResult.PARRY) {
            sp.addResource(1, Resource.Type.COMBO_POINT, this)
        }

        val event = Event(
            eventType = EventType.DAMAGE,
            damageType = Constants.DamageType.PHYSICAL,
            ability = this,
            amount = result.first,
            result = result.second,
        )
        sp.logEvent(event)

        // Proc anything that can proc off a yellow hit
        val triggerTypes = when(result.second) {
            EventResult.HIT -> listOf(Proc.Trigger.ROGUE_ANY_DAMAGING_SPECIAL, Proc.Trigger.MELEE_YELLOW_HIT, Proc.Trigger.PHYSICAL_DAMAGE)
            EventResult.CRIT -> listOf(Proc.Trigger.ROGUE_ANY_DAMAGING_SPECIAL, Proc.Trigger.MELEE_YELLOW_CRIT, Proc.Trigger.PHYSICAL_DAMAGE)
            EventResult.MISS -> listOf(Proc.Trigger.MELEE_MISS)
            EventResult.DODGE -> listOf(Proc.Trigger.MELEE_DODGE)
            EventResult.PARRY -> listOf(Proc.Trigger.MELEE_PARRY)
            EventResult.BLOCK -> listOf(Proc.Trigger.ROGUE_ANY_DAMAGING_SPECIAL, Proc.Trigger.MELEE_YELLOW_HIT, Proc.Trigger.PHYSICAL_DAMAGE)
            EventResult.BLOCKED_CRIT -> listOf(Proc.Trigger.ROGUE_ANY_DAMAGING_SPECIAL, Proc.Trigger.MELEE_YELLOW_CRIT, Proc.Trigger.PHYSICAL_DAMAGE)
            else -> null
        }

        if(triggerTypes != null) {
            sp.fireProc(triggerTypes, listOf(item), this, event)
        }
    }
}
