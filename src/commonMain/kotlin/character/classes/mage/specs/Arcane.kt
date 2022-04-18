package character.classes.mage.specs

import character.Spec
import character.SpecEpDelta
import character.Stats
import kotlin.math.max

class Arcane : Spec() {
    override val name: String = "Arcane"
    override val epBaseStat: SpecEpDelta = spellPowerBase
    override val epStatDeltas: List<SpecEpDelta> = listOf(Triple("spirit", Stats(spirit = 50), 50.0)) +
            defaultCasterDeltas


    override fun redSocketEp(deltas: Map<String, Double>): Double {
        // 12 spell dmg
        return 12.0
    }

    override fun yellowSocketEp(deltas: Map<String, Double>): Double {
        // 10 int
        return ((deltas["intellect"] ?: 0.0) * 10.0)
    }

    override fun blueSocketEp(deltas: Map<String, Double>): Double {
        // 5 int (+mp5, worth nearly nothing) or 10 spirit, whichever turns out to be better.
        return max((deltas["intellect"] ?: 0.0) * 5.0, (deltas["spirit"] ?: 0.0) * 10.0)
    }
}
