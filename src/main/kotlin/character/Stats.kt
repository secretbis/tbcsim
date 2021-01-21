package character

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

    var resilienceRating: Double = 0.0,

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

    var healthMultiplier: Double = 1.0,
    var manaMultiplier: Double = 1.0
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

        resilienceRating += stats.resilienceRating

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

        manaMultiplier *= stats.manaMultiplier

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

        resilienceRating -= stats.resilienceRating

        armorMultiplier /= stats.armorMultiplier

        physicalHasteMultiplier /= stats.physicalHasteMultiplier
        spellHasteMultiplier /= stats.spellHasteMultiplier

        whiteDamageFlatModifier -= stats.whiteDamageFlatModifier
        whiteDamageMultiplier /= stats.whiteDamageMultiplier
        whiteDamageAddlCritMultiplier /= stats.whiteDamageAddlCritMultiplier
        whiteDamageAddlOffHandPenaltyMultiplier /= stats.whiteDamageAddlOffHandPenaltyMultiplier

        yellowDamageFlatModifier += stats.yellowDamageFlatModifier
        yellowDamageMultiplier /= stats.yellowDamageMultiplier
        yellowDamageAddlCritMultiplier /= stats.yellowDamageAddlCritMultiplier
        yellowDamageAddlOffHandPenaltyMultiplier /= stats.yellowDamageAddlOffHandPenaltyMultiplier

        spellDamageFlatModifier += stats.spellDamageFlatModifier
        spellDamageMultiplier /= stats.spellDamageMultiplier
        spellDamageAddlCritMultiplier /= stats.spellDamageAddlCritMultiplier

        manaMultiplier /= stats.manaMultiplier

        return this
    }
}
