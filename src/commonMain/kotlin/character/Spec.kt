package character

import mechanics.Rating

typealias SpecEpDelta = Triple<String, Stats, Double>

abstract class Spec {
    companion object {
        // Default melee stat weights
        val attackPowerBase: SpecEpDelta = Triple("attackPower", Stats(attackPower = 100), 100.0)
        val defaultMeleeDeltas: List<SpecEpDelta> = listOf(
            Triple("strength", Stats(strength = 50), 50.0),
            Triple("agility", Stats(agility = 50), 50.0),
            Triple("meleeCritRating", Stats(meleeCritRating = 5.0 * Rating.critPerPct), 5.0 * Rating.critPerPct),
            Triple("physicalHitRating", Stats(physicalHitRating = 2.0 * Rating.physicalHitPerPct), 2.0 * Rating.physicalHitPerPct),
            Triple("physicalHasteRating", Stats(physicalHasteRating = 5.0 * Rating.hastePerPct), 5.0 * Rating.hastePerPct),
            Triple("expertiseRating", Stats(expertiseRating = 2.0 * Rating.expertisePerPct), 2.0 * Rating.expertisePerPct),
            Triple("armorPen", Stats(armorPen = 100), 100.0),
        )

        // Hunters
        val rangedAttackPowerBase: SpecEpDelta = Triple("rangedAttackPower", Stats(rangedAttackPower = 100), 100.0)
        val defaultRangedDeltas: List<SpecEpDelta> = listOf(
            Triple("agility", Stats(agility = 50), 50.0),
            Triple("rangedCritRating", Stats(rangedCritRating = 5.0 * Rating.critPerPct), 5.0 * Rating.critPerPct),
            Triple("physicalHitRating", Stats(physicalHitRating = 2.0 * Rating.physicalHitPerPct), 2.0 * Rating.physicalHitPerPct),
            Triple("physicalHasteRating", Stats(physicalHasteRating = 5.0 * Rating.hastePerPct), 5.0 * Rating.hastePerPct),
            Triple("armorPen", Stats(armorPen = 100), 100.0)
        )

        // Default spellcaster stat weights
        val spellPowerBase: SpecEpDelta = Triple("spellDamage", Stats(spellDamage = 100), 100.0)
        // AKA Enhancement Shaman
        val casterHybridDeltas = listOf(
            Triple("spellCritRating", Stats(spellCritRating = 5.0 * Rating.critPerPct), 5.0 * Rating.critPerPct),
            Triple("spellHitRating", Stats(spellHitRating = 2.0 * Rating.spellHitPerPct), 2.0 * Rating.spellHitPerPct)
        )
        val defaultCasterDeltas: List<SpecEpDelta> = listOf(
            Triple("intellect", Stats(intellect = 50), 50.0),
            Triple("spellHasteRating", Stats(spellHasteRating = 10.0 * Rating.hastePerPct), 10.0 * Rating.hastePerPct),
        ) + casterHybridDeltas
    }
    abstract val name: String

    // Used for EP calculation
    // Format is Name, Stat modifier, EP divisor
    abstract val epBaseStat: SpecEpDelta
    abstract val epStatDeltas: List<SpecEpDelta>
    open val benefitsFromMeleeWeaponDps: Boolean = false
    open val benefitsFromRangedWeaponDps: Boolean = false

    // Socket
    abstract fun redSocketEp(deltas: Map<String, Double>): Double
    abstract fun yellowSocketEp(deltas: Map<String, Double>): Double
    abstract fun blueSocketEp(deltas: Map<String, Double>): Double
    // TODO: This should be simmed, but it gets a little weird depending on the gem selected
    open fun metaSocketEp(deltas: Map<String, Double>): Double = 100.0
}
