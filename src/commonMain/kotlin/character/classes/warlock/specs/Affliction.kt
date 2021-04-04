package character.classes.warlock.specs

import character.Spec
import character.SpecEpDelta

class Affliction : Spec() {
    override val name: String = "Affliction"
    override val epBaseStat: SpecEpDelta = spellPowerBase
    override val epStatDeltas: List<SpecEpDelta> = defaultCasterDeltas
}
