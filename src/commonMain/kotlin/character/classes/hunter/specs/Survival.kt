package character.classes.hunter.specs

import character.Spec
import character.SpecEpDelta

class Survival : Spec() {
    override val name: String = "Survival"
    override val epBaseStat: SpecEpDelta = rangedAttackPowerBase
    override val epStatDeltas: List<SpecEpDelta> = defaultRangedDeltas
    override val benefitsFromRangedWeaponDps = true
}
