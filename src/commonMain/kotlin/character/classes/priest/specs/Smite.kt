package character.classes.priest.specs

import character.Spec
import character.SpecEpDelta

class Smite : Spec() {
    override val name: String = "Smite"
    override val epBaseStat: SpecEpDelta = spellPowerBase
    override val epStatDeltas: List<SpecEpDelta> = defaultCasterDeltas

    override fun redSocketEp(deltas: Map<String, Double>): Double {
        // 9 spell dmg
        return 9.0
    }

    override fun yellowSocketEp(deltas: Map<String, Double>): Double {
        // 4 spell hit + 5 spell dmg
        return 11.16
    }

    override fun blueSocketEp(deltas: Map<String, Double>): Double {
        // 8 spirit
        return 8.712
    }
}
