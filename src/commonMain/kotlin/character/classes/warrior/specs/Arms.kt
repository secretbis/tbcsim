package character.classes.warrior.specs

import character.Spec
import character.SpecEpDelta

class Arms : Spec() {
    override val name: String = "Arms"
    override val epBaseStat: SpecEpDelta = attackPowerBase
    override val epStatDeltas: List<SpecEpDelta> = defaultMeleeDeltas
    override val benefitsFromMeleeWeaponDps = true
}
