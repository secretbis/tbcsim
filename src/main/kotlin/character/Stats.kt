package character

data class Stats(
    // Base stats
    var strength: Int = 0,
    var agility: Int = 0,
    var stamina: Int = 0,
    var intellect: Int = 0,
    var spirit: Int = 0,

    // Secondary stats
    var attackPower: Int = 0,
    var physicalCritRating: Int = 0,
    var physicalHitRating: Int = 0,
    var physicalHasteRating: Int = 0,
    var expertiseRating: Int = 0,
    var armorPen: Int = 0,

    var spellCritRating: Int = 0,
    var spellHitRating: Int = 0,
    var spellHasteRating: Int = 0,
    var spellDamage: Int = 0,
    var spellHealing: Int = 0,
    var spellPen: Int = 0,

    var resilienceRating: Int = 0
) {
    fun add(stats: Stats) : Stats {
        strength += stats.strength
        agility += stats.agility
        stamina += stats.stamina
        intellect += stats.intellect
        spirit += stats.intellect

        attackPower += stats.attackPower
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

        return this
    }

    fun subtract(stats: Stats) : Stats {
        strength -= stats.strength
        agility -= stats.agility
        stamina -= stats.stamina
        intellect -= stats.intellect
        spirit -= stats.intellect

        attackPower -= stats.attackPower
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

        resilienceRating += stats.resilienceRating

        return this
    }
}
