package entity

data class Stats(val entity: Entity) {
    // Base stats
    var strength: Int = 0
    var agility: Int = 0
    var stamina: Int = 0
    var intellect: Int = 0
    var spirit: Int = 0

    // Secondary stats
    var critRating: Int = 0
    var hitRating: Int = 0
    var hasteRating: Int = 0
    var expertiseRating: Int = 0
    var armorPen: Int = 0
    var resilienceRating: Int = 0
    var spellDamage: Int = 0
    var spellHealing: Int = 0
    var spellPen: Int = 0

    fun add(stats: Stats) {
        strength += stats.strength
        agility += stats.agility
        stamina += stats.stamina
        intellect += stats.intellect
        spirit += stats.intellect

        critRating += stats.critRating
        hitRating += stats.hitRating
        hasteRating += stats.hasteRating
        expertiseRating += stats.expertiseRating
        armorPen += stats.armorPen
        resilienceRating += stats.resilienceRating
        spellDamage += stats.spellDamage
        spellHealing += stats.spellHealing
        spellPen += stats.spellPen
    }

    fun subtract(stats: Stats) {
        strength -= stats.strength
        agility -= stats.agility
        stamina -= stats.stamina
        intellect -= stats.intellect
        spirit -= stats.intellect

        critRating -= stats.critRating
        hitRating -= stats.hitRating
        hasteRating -= stats.hasteRating
        expertiseRating -= stats.expertiseRating
        armorPen =- stats.armorPen
        resilienceRating -= stats.resilienceRating
        spellDamage -= stats.spellDamage
        spellHealing -= stats.spellHealing
        spellPen -= stats.spellPen
    }
}