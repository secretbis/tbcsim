package character.classes.shaman.specs

import character.Spec
import character.SpecEpDelta

class Enhancement : Spec() {
    override val name: String = "Enhancement"
    override val epBaseStat: SpecEpDelta = attackPowerBase
    // Enhance can theoretically make use of basically every stat
    override val epStatDeltas: List<SpecEpDelta> = dualWieldMeleeDeltas + casterHybridDeltas + spellPowerBase
}
