package character.classes.druid.specs

import character.Spec
import character.SpecEpDelta

/**
 */
class Boomkin : Spec() {
    override val name: String = "Boomkin"
    override val epBaseStat: SpecEpDelta = spellPowerBase
    override val epStatDeltas: List<SpecEpDelta> = defaultCasterDeltas

    override fun redSocketEp(deltas: Map<String, Double>): Double {
        // 12 spell dmg
        return 12.0
    }

    override fun yellowSocketEp(deltas: Map<String, Double>): Double {
        // 5 hit rating / 6 spell dmg
        return ((deltas["spellHitRating"] ?: 0.0) * 5.0) + 6.0
    }

    override fun blueSocketEp(deltas: Map<String, Double>): Double {
        // 6 spell dmg
        return 6.0
    }
}