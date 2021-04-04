package character.classes.hunter.pet

import character.Spec
import character.SpecEpDelta

class HunterPetSpec : Spec() {
    override val name: String = "Hunter Pet"
    override val epBaseStat: SpecEpDelta = attackPowerBase
    override val epStatDeltas: List<SpecEpDelta> = defaultMeleeDeltas
}
