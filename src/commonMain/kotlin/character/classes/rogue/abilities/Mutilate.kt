package character.classes.rogue.abilities

import character.*
import mechanics.Melee
import sim.Event
import sim.SimParticipant
import data.Constants
import character.classes.rogue.talents.*
import character.classes.rogue.debuffs.*
import mu.KotlinLogging
import data.itemsets.SlayersArmor
import sim.EventResult
import sim.EventType

class Mutilate : Ability() {
    companion object {
        const val name = "Mutilate"
        const val secondaryName = "Mutilate (Offhand)"
    }

    override val id: Int = 34413
    override val name: String = Companion.name
    val secondaryName: String = Companion.secondaryName

    override fun gcdMs(sp: SimParticipant): Int = sp.physicalGcd().toInt()

    override fun resourceType(sp: SimParticipant): Resource.Type = Resource.Type.ENERGY
    override fun resourceCost(sp: SimParticipant): Double = 60.0

    override fun available(sp: SimParticipant): Boolean {
        val mutilate = sp.character.klass.talents[character.classes.rogue.talents.Mutilate.name] as character.classes.rogue.talents.Mutilate?
        val available = if(mutilate != null){ mutilate.currentRank == mutilate.maxRank } else { false }
        if (!available) {
            KotlinLogging.logger{}.debug{ "Tried to use ability $name without having the corresponding talent" }
        }

        val usesDaggers = if(sp.isDualWielding()) {
            (sp.character.gear.mainHand.itemSubclass == Constants.ItemSubclass.DAGGER) && (sp.character.gear.offHand.itemSubclass == Constants.ItemSubclass.DAGGER)
        } else {
            false
        }
        if (!usesDaggers) {
            KotlinLogging.logger{}.debug{ "Tried to use ability $name without having a daggers in both hands" }
        }

        return available && usesDaggers && super.available(sp)
    }

    fun castMainhand(sp: SimParticipant, bonusDamage: Double, poisonMultiplier: Double) {

        val pw = sp.character.klass.talents[PuncturingWounds.name] as PuncturingWounds?
        val increasedCritChance = pw?.additionalCritChanceMutilate() ?: 0.0

        val lethality = sp.character.klass.talents[Lethality.name] as Lethality?
        val critDmgMultiplier = lethality?.critDamageMultiplier() ?: 1.0

        val opportunity = sp.character.klass.talents[Opportunity.name] as Opportunity?
        var increasedDamagePercent = opportunity?.damageIncreasePercent() ?: 0.0
        val slayers = sp.buffs[SlayersArmor.FOUR_SET_BUFF_NAME]
        increasedDamagePercent += if (slayers != null) { SlayersArmor.fourSetGeneratorDamageIncreasePercent() } else 0.0

        val dmgMultiplier = 1 + (increasedDamagePercent / 100.0).coerceAtLeast(0.0)

        // TODO: not sure if correct with the multiplier
        // also unsure about the poisonMultiplier (additive/multiplicative)
        val item = sp.character.gear.mainHand
        val damageRoll = (Melee.baseDamageRoll(sp, item) + bonusDamage) * dmgMultiplier * poisonMultiplier
        val result = Melee.attackRoll(sp, damageRoll, item, isWhiteDmg = false, bonusCritChance = increasedCritChance, abilityAdditionalCritDamageMultiplier = critDmgMultiplier)

        if(result.second != EventResult.MISS && result.second != EventResult.DODGE && result.second != EventResult.PARRY) {
            sp.addResource(2, Resource.Type.COMBO_POINT, name)
        }

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

    fun castOffhand(sp: SimParticipant, bonusDamage: Double, poisonMultiplier: Double) {
        val pw = sp.character.klass.talents[PuncturingWounds.name] as PuncturingWounds?
        val increasedCritChance = pw?.additionalCritChanceMutilate() ?: 0.0

        val lethality = sp.character.klass.talents[Lethality.name] as Lethality?
        val critDmgMultiplier = lethality?.critDamageMultiplier() ?: 1.0

        val opportunity = sp.character.klass.talents[Opportunity.name] as Opportunity?
        var increasedDamagePercent = opportunity?.damageIncreasePercent() ?: 0.0
        val slayers = sp.buffs[SlayersArmor.FOUR_SET_BUFF_NAME]
        increasedDamagePercent += if (slayers != null) { SlayersArmor.fourSetGeneratorDamageIncreasePercent() } else 0.0

        val dmgMultiplier = 1 + (increasedDamagePercent / 100.0).coerceAtLeast(0.0)

        // TODO: not sure if correct with the multiplier
        // also unsure about the poisonMultiplier (additive/multiplicative)
        val item = sp.character.gear.offHand
        val damageRoll = (Melee.baseDamageRoll(sp, item) + bonusDamage) * dmgMultiplier * poisonMultiplier
        val result = Melee.attackRoll(sp, damageRoll, item, isWhiteDmg = false, bonusCritChance = increasedCritChance, abilityAdditionalCritDamageMultiplier = critDmgMultiplier)

        val event = Event(
            eventType = EventType.DAMAGE,
            damageType = Constants.DamageType.PHYSICAL,
            abilityName = secondaryName,
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

    fun poisonsPresentOnTarget(sp: SimParticipant): Boolean {
        val dpDot = sp.sim.target.debuffs[DeadlyPoisonDot.name]
        val wpDot = sp.sim.target.debuffs[character.classes.rogue.debuffs.WoundPoison.name]
        return dpDot != null || wpDot != null
    }

    override fun cast(sp: SimParticipant) {
        val poisonMultiplier = if(poisonsPresentOnTarget(sp)) { 1.5 } else { 1.0 }
        val bonusDamage = 110.0
        castMainhand(sp, bonusDamage, poisonMultiplier)
        castOffhand(sp, bonusDamage, poisonMultiplier)
        sp.addResource(2, Resource.Type.COMBO_POINT, name)
    }
}
