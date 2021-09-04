package character.classes.priest.pet

import character.Spec
import character.SpecEpDelta

class ShadowfiendSpec : Spec() {
    override val name: String = "Shadowfiend"
    override val epBaseStat: SpecEpDelta = attackPowerBase
    override val epStatDeltas: List<SpecEpDelta> = defaultMeleeDeltas

    override fun redSocketEp(deltas: Map<String, Double>): Double = 0.0
    override fun yellowSocketEp(deltas: Map<String, Double>): Double = 0.0
    override fun blueSocketEp(deltas: Map<String, Double>): Double = 0.0
}
