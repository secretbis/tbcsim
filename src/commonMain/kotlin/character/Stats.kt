package character

import data.Constants.StatType

data class Stats(
    // Base stats
    var strength: Int = 0,
    var agility: Int = 0,
    var stamina: Int = 0,
    var intellect: Int = 0,
    var spirit: Int = 0,

    var strengthMultiplier: Double = 1.0,
    var agilityMultiplier: Double = 1.0,
    var staminaMultiplier: Double = 1.0,
    var intellectMultiplier: Double = 1.0,
    var spiritMultiplier: Double = 1.0,

    var armor: Int = 0,

    // Secondary stats
    var attackPower: Int = 0,
    var rangedAttackPower: Int = 0,
    var feralAttackPower: Int = 0,

    var meleeCritRating: Double = 0.0,
    var rangedCritRating: Double = 0.0,

    var physicalHitRating: Double = 0.0,
    var physicalHasteRating: Double = 0.0,
    var expertiseRating: Double = 0.0,
    var armorPen: Int = 0,

    var spellCritRating: Double = 0.0,
    var spellHitRating: Double = 0.0,
    var spellHasteRating: Double = 0.0,
    var spellDamage: Int = 0,
    var spellHealing: Int = 0,
    var spellPen: Int = 0,

    var holyDamage: Int = 0,
    var fireDamage: Int = 0,
    var natureDamage: Int = 0,
    var frostDamage: Int = 0,
    var shadowDamage: Int = 0,
    var arcaneDamage: Int = 0,

    // Defensive stats
    var resilienceRating: Double = 0.0,
    var defenseRating: Double = 0.0,
    var blockValue: Double = 0.0,
    var blockRating: Double = 0.0,
    var dodgeRating: Double = 0.0,
    var parryRating: Double = 0.0,

    // Resistances
    var fireResistance: Int = 0,
    var natureResistance: Int = 0,
    var frostResistance: Int = 0,
    var shadowResistance: Int = 0,
    var arcaneResistance: Int = 0,

    // ItemClass specific modifiers
    var bowCritRating: Double = 0.0,
    var gunCritRating: Double = 0.0,
    var swordExpertiseRating: Double = 0.0,
    var maceExpertiseRating: Double = 0.0,
    var axeExpertiseRating: Double = 0.0,

    // Generic (non-spell-specific) modifiers
    var armorMultiplier: Double = 1.0,

    var attackPowerMultiplier: Double = 1.0,
    var rangedAttackPowerMultiplier: Double = 1.0,

    var physicalHasteMultiplier: Double = 1.0,
    var spellHasteMultiplier: Double = 1.0,

    var whiteDamageFlatModifier: Double = 0.0,
    var whiteDamageMultiplier: Double = 1.0,
    var whiteDamageAddlCritMultiplier: Double = 1.0,
    var whiteDamageAddlOffHandPenaltyModifier: Double = 0.0,

    var yellowDamageFlatModifier: Double = 0.0,
    var yellowDamageMultiplier: Double = 1.0,
    var yellowDamageAddlCritMultiplier: Double = 1.0,
    var yellowDamageAddlOffHandPenaltyModifier: Double = 0.0,

    var spellDamageFlatModifier: Double = 0.0,
    var spellDamageMultiplier: Double = 1.0,
    var spellDamageAddlCritMultiplier: Double = 1.0,

    var physicalDamageMultiplier: Double = 1.0,
    var holyDamageMultiplier: Double = 1.0,
    var fireDamageMultiplier: Double = 1.0,
    var natureDamageMultiplier: Double = 1.0,
    var frostDamageMultiplier: Double = 1.0,
    var shadowDamageMultiplier: Double = 1.0,
    var arcaneDamageMultiplier: Double = 1.0,
    var petDamageMultiplier: Double = 1.0,

    var healthMultiplier: Double = 1.0,
    var healthFlatModifier: Int = 0,
    var manaMultiplier: Double = 1.0,
    var manaFlatModifier: Int = 0,
    var manaPer5Seconds: Int = 0,

    // Weird abilities
    var offHandAddlWhiteHitPct: Double = 0.0
) {
    companion object {
        const val physicalCritMultiplier: Double = 2.0
        const val spellCritMultiplier: Double = 1.5
        const val offHandPenalty: Double = 0.5
    }

    fun add(stats: Stats) : Stats {
        strength += stats.strength
        agility += stats.agility
        stamina += stats.stamina
        intellect += stats.intellect
        spirit += stats.spirit

        strengthMultiplier *= stats.strengthMultiplier
        agilityMultiplier *= stats.agilityMultiplier
        staminaMultiplier *= stats.staminaMultiplier
        intellectMultiplier *= stats.intellectMultiplier
        spiritMultiplier *= stats.spiritMultiplier

        armor += stats.armor

        attackPower += stats.attackPower
        rangedAttackPower += stats.rangedAttackPower
        feralAttackPower += stats.feralAttackPower

        meleeCritRating += stats.meleeCritRating
        rangedCritRating += stats.rangedCritRating

        physicalHitRating += stats.physicalHitRating
        physicalHasteRating += stats.physicalHasteRating
        expertiseRating += stats.expertiseRating
        armorPen += stats.armorPen

        spellCritRating += stats.spellCritRating
        spellHitRating += stats.spellHitRating
        spellHasteRating += stats.spellHasteRating
        spellDamage += stats.spellDamage
        spellHealing += stats.spellHealing
        spellPen += stats.spellPen

        holyDamage += stats.holyDamage
        fireDamage += stats.fireDamage
        natureDamage += stats.natureDamage
        frostDamage += stats.frostDamage
        shadowDamage += stats.shadowDamage
        arcaneDamage += stats.arcaneDamage

        resilienceRating += stats.resilienceRating
        defenseRating += stats.defenseRating
        blockValue += stats.blockValue
        blockRating += stats.blockRating
        dodgeRating += stats.dodgeRating
        parryRating += stats.parryRating

        fireResistance = (fireResistance + stats.fireResistance).coerceAtLeast(0)
        natureResistance = (natureResistance + stats.natureResistance).coerceAtLeast(0)
        frostResistance = (frostResistance + stats.frostResistance).coerceAtLeast(0)
        shadowResistance = (shadowResistance + stats.shadowResistance).coerceAtLeast(0)
        arcaneResistance = (arcaneResistance + stats.arcaneResistance).coerceAtLeast(0)

        bowCritRating += stats.bowCritRating
        gunCritRating += stats.gunCritRating
        swordExpertiseRating += stats.swordExpertiseRating
        maceExpertiseRating += stats.maceExpertiseRating
        axeExpertiseRating += stats.axeExpertiseRating

        armorMultiplier *= stats.armorMultiplier

        attackPowerMultiplier *= stats.attackPowerMultiplier
        rangedAttackPowerMultiplier *= stats.rangedAttackPowerMultiplier

        physicalHasteMultiplier *= stats.physicalHasteMultiplier
        spellHasteMultiplier *= stats.spellHasteMultiplier

        whiteDamageFlatModifier += stats.whiteDamageFlatModifier
        whiteDamageMultiplier *= stats.whiteDamageMultiplier
        whiteDamageAddlCritMultiplier *= stats.whiteDamageAddlCritMultiplier
        whiteDamageAddlOffHandPenaltyModifier += stats.whiteDamageAddlOffHandPenaltyModifier

        yellowDamageFlatModifier += stats.yellowDamageFlatModifier
        yellowDamageMultiplier *= stats.yellowDamageMultiplier
        yellowDamageAddlCritMultiplier *= stats.yellowDamageAddlCritMultiplier
        yellowDamageAddlOffHandPenaltyModifier += stats.yellowDamageAddlOffHandPenaltyModifier

        spellDamageFlatModifier += stats.spellDamageFlatModifier
        spellDamageMultiplier *= stats.spellDamageMultiplier
        spellDamageAddlCritMultiplier *= stats.spellDamageAddlCritMultiplier

        physicalDamageMultiplier *= stats.physicalDamageMultiplier
        holyDamageMultiplier *= stats.holyDamageMultiplier
        fireDamageMultiplier *= stats.fireDamageMultiplier
        natureDamageMultiplier *= stats.natureDamageMultiplier
        frostDamageMultiplier *= stats.frostDamageMultiplier
        shadowDamageMultiplier *= stats.shadowDamageMultiplier
        arcaneDamageMultiplier *= stats.arcaneDamageMultiplier
        petDamageMultiplier *= stats.petDamageMultiplier

        healthMultiplier *= stats.healthMultiplier
        healthFlatModifier += stats.healthFlatModifier
        manaMultiplier *= stats.manaMultiplier
        manaFlatModifier += stats.manaFlatModifier
        manaPer5Seconds += stats.manaPer5Seconds

        offHandAddlWhiteHitPct += stats.offHandAddlWhiteHitPct

        return this
    }

    // Serves as a way to update a Stats object given a DB enum constant and a value
    // This function mutates its first argument
    fun addByStatType(statType: StatType, value: Int): Stats {
        // TODO: Reorder this into something that makes sense
        when (statType) {
            StatType.AGILITY ->
                agility += value
            StatType.CRIT_RATING -> {
                meleeCritRating += value
                rangedCritRating += value
            }
            StatType.CRIT_MELEE_RATING ->
                meleeCritRating += value
            StatType.CRIT_RANGED_RATING ->
                rangedCritRating
            StatType.CRIT_SPELL_RATING ->
                spellCritRating += value
            StatType.EXPERTISE_RATING ->
                expertiseRating += value
            StatType.HASTE_RATING, StatType.HASTE_MELEE_RATING, StatType.HASTE_RANGED_RATING ->
                physicalHasteRating += value
            StatType.HASTE_SPELL_RATING ->
                spellHasteRating += value
            StatType.HIT_RATING, StatType.HIT_MELEE_RATING, StatType.HIT_RANGED_RATING ->
                physicalHitRating += value
            StatType.HIT_SPELL_RATING ->
                spellHitRating += value
            StatType.INTELLECT ->
                intellect += value
            StatType.SPIRIT ->
                spirit += value
            StatType.STAMINA ->
                stamina += value
            StatType.STRENGTH ->
                strength += value
            StatType.DEFENSE_SKILL_RATING ->
                defenseRating += value
            StatType.BLOCK_VALUE ->
                blockValue += value
            StatType.BLOCK_RATING ->
                blockRating += value
            StatType.DODGE_RATING ->
                dodgeRating += value
            StatType.PARRY_RATING ->
                parryRating += value
            StatType.MANA ->
                manaFlatModifier += value
            StatType.HEALTH ->
                healthFlatModifier += value
            StatType.MANA_PER_5_SECONDS ->
                manaPer5Seconds += value
            StatType.RESILIENCE_RATING ->
                resilienceRating += value
            else -> {
                // Do nothing
            }
        }

        return this
    }
}
