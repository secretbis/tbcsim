package character.classes.warrior.specs

import character.Spec
import character.SpecEpDelta

class Fury : Spec() {
    override val name: String = "Fury"
    override val epBaseStat: SpecEpDelta = attackPowerBase
    override val epStatDeltas: List<SpecEpDelta> = dualWieldMeleeDeltas
}
