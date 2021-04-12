package character.classes.warlock.specs

import character.Spec
import character.SpecEpDelta

class Affliction : Spec() {
    override val name: String = "Affliction"
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
