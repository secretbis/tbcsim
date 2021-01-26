package character

import data.Constants.StatType

data class Stats(
    // Base stats
    var strength: Int = 0,
    var agility: Int = 0,
    var stamina: Int = 0,
    var intellect: Int = 0,
    var spirit: Int = 0,

    var armor: Int = 0,

    // Secondary stats
    var attackPower: Int = 0,
    var rangedAttackPower: Int = 0,

    var physicalCritRating: Double = 0.0,
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

    // Generic (non-spell-specific) modifiers
    var armorMultiplier: Double = 1.0,

    var attackPowerMultiplier: Double = 1.0,
    var rangedAttackPowerMultiplier: Double = 1.0,

    var physicalHasteMultiplier: Double = 1.0,
    var spellHasteMultiplier: Double = 1.0,

    var whiteDamageFlatModifier: Double = 0.0,
    var whiteDamageMultiplier: Double = 1.0,
    var whiteDamageAddlCritMultiplier: Double = 1.0,
    var whiteDamageAddlOffHandPenaltyMultiplier: Double = 1.0,

    var yellowDamageFlatModifier: Double = 0.0,
    var yellowDamageMultiplier: Double = 1.0,
    var yellowDamageAddlCritMultiplier: Double = 1.0,
    var yellowDamageAddlOffHandPenaltyMultiplier: Double = 1.0,

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

    var healthMultiplier: Double = 1.0,
    var healthFlatModifier: Int = 0,
    var manaMultiplier: Double = 1.0,
    var manaFlatModifier: Int = 0,
    var manaPer5Seconds: Int = 0,
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
        spirit += stats.intellect

        armor += stats.armor

        attackPower += stats.attackPower
        rangedAttackPower += stats.rangedAttackPower

        physicalCritRating += stats.physicalCritRating
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

        fireResistance += stats.fireResistance
        natureResistance += stats.natureResistance
        frostResistance += stats.frostResistance
        shadowResistance += stats.shadowResistance
        arcaneResistance += stats.arcaneResistance

        armorMultiplier *= stats.armorMultiplier

        physicalHasteMultiplier *= stats.physicalHasteMultiplier
        spellHasteMultiplier *= stats.spellHasteMultiplier

        whiteDamageFlatModifier += stats.whiteDamageFlatModifier
        whiteDamageMultiplier *= stats.whiteDamageMultiplier
        whiteDamageAddlCritMultiplier *= stats.whiteDamageAddlCritMultiplier
        whiteDamageAddlOffHandPenaltyMultiplier *= stats.whiteDamageAddlOffHandPenaltyMultiplier

        yellowDamageFlatModifier += stats.yellowDamageFlatModifier
        yellowDamageMultiplier *= stats.yellowDamageMultiplier
        yellowDamageAddlCritMultiplier *= stats.yellowDamageAddlCritMultiplier
        yellowDamageAddlOffHandPenaltyMultiplier *= stats.yellowDamageAddlOffHandPenaltyMultiplier

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

        healthMultiplier *= stats.healthMultiplier
        healthFlatModifier += stats.healthFlatModifier
        manaMultiplier *= stats.manaMultiplier
        manaFlatModifier += stats.manaFlatModifier
        manaPer5Seconds += stats.manaPer5Seconds

        return this
    }

    fun subtract(stats: Stats) : Stats {
        strength -= stats.strength
        agility -= stats.agility
        stamina -= stats.stamina
        intellect -= stats.intellect
        spirit -= stats.intellect

        armor -= stats.armor

        attackPower -= stats.attackPower
        rangedAttackPower -= stats.rangedAttackPower

        physicalCritRating -= stats.physicalCritRating
        physicalHitRating -= stats.physicalHitRating
        physicalHasteRating -= stats.physicalHasteRating
        expertiseRating -= stats.expertiseRating
        armorPen -= stats.armorPen

        spellCritRating -= stats.spellCritRating
        spellHitRating -= stats.spellHitRating
        spellHasteRating -= stats.spellHasteRating
        spellDamage -= stats.spellDamage
        spellHealing -= stats.spellHealing
        spellPen -= stats.spellPen

        holyDamage -= stats.holyDamage
        fireDamage -= stats.fireDamage
        natureDamage -= stats.natureDamage
        frostDamage -= stats.frostDamage
        shadowDamage -= stats.shadowDamage
        arcaneDamage -= stats.arcaneDamage

        resilienceRating -= stats.resilienceRating
        defenseRating -= stats.defenseRating
        blockValue -= stats.blockValue
        blockRating -= stats.blockRating
        dodgeRating -= stats.dodgeRating
        parryRating -= stats.parryRating

        fireResistance -= stats.fireResistance
        natureResistance -= stats.natureResistance
        frostResistance -= stats.frostResistance
        shadowResistance -= stats.shadowResistance
        arcaneResistance -= stats.arcaneResistance

        armorMultiplier /= stats.armorMultiplier

        physicalHasteMultiplier /= stats.physicalHasteMultiplier
        spellHasteMultiplier /= stats.spellHasteMultiplier

        whiteDamageFlatModifier -= stats.whiteDamageFlatModifier
        whiteDamageMultiplier /= stats.whiteDamageMultiplier
        whiteDamageAddlCritMultiplier /= stats.whiteDamageAddlCritMultiplier
        whiteDamageAddlOffHandPenaltyMultiplier /= stats.whiteDamageAddlOffHandPenaltyMultiplier

        yellowDamageFlatModifier -= stats.yellowDamageFlatModifier
        yellowDamageMultiplier /= stats.yellowDamageMultiplier
        yellowDamageAddlCritMultiplier /= stats.yellowDamageAddlCritMultiplier
        yellowDamageAddlOffHandPenaltyMultiplier /= stats.yellowDamageAddlOffHandPenaltyMultiplier

        spellDamageFlatModifier -= stats.spellDamageFlatModifier
        spellDamageMultiplier /= stats.spellDamageMultiplier
        spellDamageAddlCritMultiplier /= stats.spellDamageAddlCritMultiplier

        physicalDamageMultiplier /= stats.physicalDamageMultiplier
        holyDamageMultiplier /= stats.holyDamageMultiplier
        fireDamageMultiplier /= stats.fireDamageMultiplier
        natureDamageMultiplier /= stats.natureDamageMultiplier
        frostDamageMultiplier /= stats.frostDamageMultiplier
        shadowDamageMultiplier /= stats.shadowDamageMultiplier
        arcaneDamageMultiplier /= stats.arcaneDamageMultiplier

        healthMultiplier /= stats.healthMultiplier
        healthFlatModifier -= stats.healthFlatModifier
        manaMultiplier /= stats.manaMultiplier
        manaFlatModifier -= stats.manaFlatModifier
        manaPer5Seconds -= stats.manaPer5Seconds

        return this
    }

    // Serves as a way to update a Stats object given a DB enum constant and a value
    // This function mutates its first argument
    fun addByStatType(statType: StatType, value: Int): Stats {
        // TODO: Reorder this into something that makes sense
        when (statType) {
            StatType.AGILITY ->
                agility += value
            StatType.CRIT_MELEE_RATING, StatType.CRIT_RANGED_RATING ->
                physicalCritRating += value
            StatType.CRIT_RATING -> {
                physicalCritRating += value
                spellCritRating += value
            }
            StatType.CRIT_SPELL_RATING ->
                spellCritRating += value
            StatType.EXPERTISE_RATING ->
                expertiseRating += value
            StatType.HASTE_MELEE_RATING, StatType.HASTE_RANGED_RATING ->
                physicalHasteRating += value
            StatType.HASTE_RATING -> {
                physicalHasteRating += value
                spellHasteRating += value
            }
            StatType.HASTE_SPELL_RATING ->
                spellHasteRating += value
            StatType.HIT_MELEE_RATING, StatType.HIT_RANGED_RATING ->
                physicalHitRating += value
            StatType.HIT_RATING -> {
                physicalHitRating += value
                spellHitRating += value
            }
            StatType.HIT_SPELL_RATING -> {
                spellHitRating += value
            }
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
            else -> {
                // Do nothing
            }
        }

        return this
    }
}
