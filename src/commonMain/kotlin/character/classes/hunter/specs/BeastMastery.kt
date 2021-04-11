package character.classes.hunter.specs

import character.Spec
import character.SpecEpDelta

class BeastMastery : Spec() {
    override val name: String = "Beast Mastery"
    override val epBaseStat: SpecEpDelta = rangedAttackPowerBase
    override val epStatDeltas: List<SpecEpDelta> = defaultRangedDeltas
    override val benefitsFromRangedWeaponDps = true
}
