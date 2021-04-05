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
            Triple("physicalCritRating", Stats(physicalCritRating = Rating.critPerPct), Rating.critPerPct),
            Triple("physicalHasteRating", Stats(physicalHasteRating = Rating.hastePerPct), Rating.hastePerPct),
            Triple("expertiseRating", Stats(expertiseRating = Rating.expertisePerPct), Rating.expertisePerPct),
            Triple("armorPen", Stats(armorPen = 100), 100.0)
        )

        // Hit rating is only really interesting for dual-wield classes.  Other specs get cap and never think about it.
        val dualWieldMeleeDeltas = defaultMeleeDeltas +
                Triple("physicalHitRating", Stats(physicalHitRating = Rating.physicalHitPerPct), Rating.physicalHitPerPct)

        // Hunters
        val rangedAttackPowerBase: SpecEpDelta = Triple("rangedAttackPower", Stats(rangedAttackPower = 100), 100.0)
        val defaultRangedDeltas: List<SpecEpDelta> = listOf(
            // Hit rating is omitted, since it's not difficult to get 9%
            Triple("agility", Stats(agility = 50), 50.0),
            Triple("physicalCritRating", Stats(physicalCritRating = Rating.critPerPct), Rating.critPerPct),
            Triple("physicalHasteRating", Stats(physicalHasteRating = Rating.hastePerPct), Rating.hastePerPct),
            Triple("armorPen", Stats(armorPen = 100), 100.0)
        )

        // Default spellcaster stat weights
        val spellPowerBase: SpecEpDelta = Triple("spellDamage", Stats(spellDamage = 100), 100.0)
        // AKA Enhancement Shaman
        val casterHybridDeltas = listOf(
            Triple("spellCritRating", Stats(spellCritRating = Rating.critPerPct), Rating.critPerPct),
            Triple("spellHitRating", Stats(spellHitRating = Rating.spellHitPerPct), Rating.spellHitPerPct)
        )
        val defaultCasterDeltas: List<SpecEpDelta> = listOf(
            Triple("intellect", Stats(intellect = 50), 50.0),
            Triple("spellHasteRating", Stats(spellHasteRating = Rating.hastePerPct), Rating.hastePerPct),
        ) + casterHybridDeltas
    }
    abstract val name: String

    // Used for EP calculation
    // Format is Name, Stat modifier, EP divisor
    abstract val epBaseStat: SpecEpDelta
    abstract val epStatDeltas: List<SpecEpDelta>
}
