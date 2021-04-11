package character.classes.hunter.specs

import character.Spec
import character.SpecEpDelta

class Marksmanship : Spec() {
    override val name: String = "Marksmanship"
    override val epBaseStat: SpecEpDelta = rangedAttackPowerBase
    override val epStatDeltas: List<SpecEpDelta> = defaultRangedDeltas
    override val benefitsFromRangedWeaponDps = true
}
