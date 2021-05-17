package character.classes.mage.pet

import character.Spec
import character.SpecEpDelta

class WaterElementalSpec : Spec() {
    override val name: String = "Water Elemental"
    override val epBaseStat: SpecEpDelta = attackPowerBase
    override val epStatDeltas: List<SpecEpDelta> = defaultMeleeDeltas

    override fun redSocketEp(deltas: Map<String, Double>): Double = 0.0
    override fun yellowSocketEp(deltas: Map<String, Double>): Double = 0.0
    override fun blueSocketEp(deltas: Map<String, Double>): Double = 0.0
}
