package character

import mechanics.Rating

typealias SpecEpDelta = Triple<String, Stats, Double>

abstract class Spec {
    companion object {
        // Default melee stat weights
        val attackPowerBase: SpecEpDelta = Triple("Attack Power", Stats(attackPower = 100), 100.0)
        val defaultMeleeDeltas: List<SpecEpDelta> = listOf(
            Triple("Strength", Stats(strength = 50), 50.0),
            Triple("Agility", Stats(agility = 50), 50.0),
            Triple("Crit Rating", Stats(physicalCritRating = Rating.critPerPct), Rating.critPerPct),
            Triple("Haste Rating", Stats(physicalHasteRating = Rating.hastePerPct), Rating.hastePerPct),
            Triple("Hit Rating", Stats(physicalHitRating = Rating.physicalHitPerPct), Rating.physicalHitPerPct),
            Triple("Expertise Rating", Stats(expertiseRating = Rating.expertisePerPct), Rating.expertisePerPct),
            // Armor Pen is very much nonlinear, so it's probably useful to calculate several points to get an idea of the trend
            Triple("Armor Pen (+250)", Stats(armorPen = 250), 250.0),
            Triple("Armor Pen (+500)", Stats(armorPen = 500), 500.0),
            Triple("Armor Pen (+1000)", Stats(armorPen = 1000), 1000.0)
        )

        // Hunters
        val rangedAttackPowerBase: SpecEpDelta = Triple("Ranged Attack Power", Stats(rangedAttackPower = 100), 100.0)
        val defaultRangedDeltas: List<SpecEpDelta> = listOf(
            Triple("Agility", Stats(agility = 50), 50.0),
            Triple("Crit Rating", Stats(physicalCritRating = Rating.critPerPct), Rating.critPerPct),
            Triple("Haste Rating", Stats(physicalHasteRating = Rating.hastePerPct), Rating.hastePerPct),
            Triple("Hit Rating", Stats(physicalHitRating = Rating.physicalHitPerPct), Rating.physicalHitPerPct),
            // Armor Pen is very much nonlinear, so it's probably useful to calculate several points to get an idea of the trend
            Triple("Armor Pen (+250)", Stats(armorPen = 250), 250.0),
            Triple("Armor Pen (+500)", Stats(armorPen = 500), 500.0),
            Triple("Armor Pen (+1000)", Stats(armorPen = 1000), 1000.0)
        )

        // Default spellcaster stat weights
        val spellPowerBase: SpecEpDelta = Triple("Spell Power", Stats(spellDamage = 100), 100.0)
        val defaultCasterDeltas: List<SpecEpDelta> = listOf(
            Triple("Intellect", Stats(intellect = 50), 50.0),
            Triple("Spell Crit Rating", Stats(spellCritRating = Rating.critPerPct), Rating.critPerPct),
            Triple("Spell Haste Rating", Stats(spellHasteRating = Rating.hastePerPct), Rating.hastePerPct),
            Triple("Spell Hit Rating", Stats(spellHitRating = Rating.spellHitPerPct), Rating.spellHitPerPct)
        )
    }
    abstract val name: String

    // Used for EP calculation
    // Format is Name, Stat modifier, EP divisor
    abstract val epBaseStat: SpecEpDelta
    abstract val epStatDeltas: List<SpecEpDelta>
}
