package character.classes.mage.specs

import character.Spec
import character.SpecEpDelta
import character.Stats

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
        // 5 spell haste rating / 6 spell damage
        return ((deltas["intellect"] ?: 0.0) * 10.0)
    }

    override fun blueSocketEp(deltas: Map<String, Double>): Double {
        // 6 spell dmg
        return 6.0
    }
}
