package character.classes.boss.specs

import character.Spec
import character.SpecEpDelta

class BossSpec : Spec() {
    override val name: String = "Boss"
    override val epBaseStat: SpecEpDelta = attackPowerBase
    override val epStatDeltas: List<SpecEpDelta> = defaultMeleeDeltas
}
