package character.classes.warlock.specs

import character.Spec
import character.SpecEpDelta

class Destruction : Spec() {
    override val name: String = "Destruction"
    override val epBaseStat: SpecEpDelta = spellPowerBase
    override val epStatDeltas: List<SpecEpDelta> = defaultCasterDeltas
}
