package character.classes.shaman.specs

import character.Spec
import character.SpecEpDelta

class Elemental : Spec() {
    override val name: String = "Elemental"
    override val epBaseStat: SpecEpDelta = spellPowerBase
    override val epStatDeltas: List<SpecEpDelta> = defaultCasterDeltas
}
