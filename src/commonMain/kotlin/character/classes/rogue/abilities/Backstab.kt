package character.classes.rogue.abilities

import character.*
import mechanics.Melee
import sim.Event
import sim.SimParticipant
import data.Constants
import character.classes.rogue.talents.*
import io.github.oshai.kotlinlogging.KotlinLogging
import data.itemsets.SlayersArmor
import sim.EventResult
import sim.EventType

class Backstab : Ability() {
    companion object {
        const val name = "Backstab"
    }

    override val id: Int = 26863
    override val name: String = Companion.name
    override val icon: String = "ability_backstab.jpg"

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

        val aggression = sp.character.klass.talents[Aggression.name] as Aggression?
        var increasedDamagePercent = aggression?.damageIncreasePercent() ?: 0.0
        val surprise = sp.character.klass.talents[SurpriseAttacks.name] as SurpriseAttacks?
        increasedDamagePercent += surprise?.damageIncreasePercent() ?: 0.0
        val opportunity = sp.character.klass.talents[Opportunity.name] as Opportunity?
        increasedDamagePercent += opportunity?.damageIncreasePercent() ?: 0.0
        val slayers = sp.buffs[SlayersArmor.FOUR_SET_BUFF_NAME]
        increasedDamagePercent += if (slayers != null) { SlayersArmor.fourSetGeneratorDamageIncreasePercent() } else 0.0

        val dmgMultiplier = 1 + (increasedDamagePercent / 100.0).coerceAtLeast(0.0)

        val item = sp.character.gear.mainHand

        // TODO: not sure if correct
        val damageRoll = (Melee.baseDamageRoll(sp, item, isNormalized = true) * weaponDamageMultiplier * dmgMultiplier) + bonusDamage
        val result = Melee.attackRoll(sp, damageRoll, item, isWhiteDmg = false, bonusCritChance = increasedCritChance, abilityAdditionalCritDamageMultiplier = critDmgMultiplier)

        if(result.second != EventResult.MISS && result.second != EventResult.DODGE) {
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
