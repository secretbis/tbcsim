package character.classes.rogue.abilities

import character.*
import mechanics.Melee
import sim.Event
import sim.SimParticipant
import data.Constants
import character.classes.rogue.talents.*
import mu.KotlinLogging

class Backstab : Ability() {
    companion object {
        const val name = "Backstab"
    }

    override val id: Int = 26863
    override val name: String = Companion.name

    override fun gcdMs(sp: SimParticipant): Int = sp.physicalGcd().toInt()

    override fun resourceType(sp: SimParticipant): Resource.Type = Resource.Type.ENERGY
    override fun resourceCost(sp: SimParticipant): Double = 60.0

    override fun available(sp: SimParticipant): Boolean {
        val usesDagger = sp.character.gear.mainHand.itemSubclass == Constants.ItemSubclass.DAGGER
        if (!usesDagger) {
            KotlinLogging.logger{}.debug{ "Tried to use ability $name without having a dagger in the mainhand" }
        }
        return usesDagger && super.available(sp)
    }

    val bonusDamage = 255
    val weaponDamageMultiplier = 1.50
    override fun cast(sp: SimParticipant) {

        val pw = sp.character.klass.talents[PuncturingWounds.name] as PuncturingWounds?
        val increasedCritChance = pw?.additionalCritChanceBackstab() ?: 0.0

        val lethality = sp.character.klass.talents[Lethality.name] as Lethality?
        val critDmgMultiplier = lethality?.critDamageMultiplier() ?: 1.0

        val aggression = sp.character.klass.talents[Aggression.name] as ImprovedEviscerate?
        var increasedDamagePercent = aggression?.damageIncreasePercent() ?: 0.0
        val surprise = sp.character.klass.talents[SurpriseAttacks.name] as SurpriseAttacks?
        increasedDamagePercent += surprise?.damageIncreasePercent() ?: 0.0
        val opportunity = sp.character.klass.talents[Opportunity.name] as Opportunity?
        increasedDamagePercent += opportunity?.damageIncreasePercent() ?: 0.0
        
        val dmgMultiplier = 1 + (increasedDamagePercent / 100.0).coerceAtLeast(0.0)

        val item = sp.character.gear.mainHand
        
        // TODO: not sure if correct
        val damageRoll = (Melee.baseDamageRoll(sp, item, isNormalized = true) * weaponDamageMultiplier * dmgMultiplier) + bonusDamage
        val result = Melee.attackRoll(sp, damageRoll, item, isWhiteDmg = false, bonusCritChance = increasedCritChance, abilityAdditionalCritDamageMultiplier = critDmgMultiplier)
        
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
