package character.classes.hunter.specs

import character.Spec
import character.SpecEpDelta

class Marksmanship : Spec() {
    override val name: String = "Marksmanship"
    override val epBaseStat: SpecEpDelta = rangedAttackPowerBase
    override val epStatDeltas: List<SpecEpDelta> = defaultRangedDeltas
    override val benefitsFromRangedWeaponDps = true

    override fun redSocketEp(deltas: Map<String, Double>): Double {
        // 10 agi
        return (deltas["agility"] ?: 0.0) * 10.0
    }

    override fun yellowSocketEp(deltas: Map<String, Double>): Double {
        // 10 crit rating
        return (deltas["rangedCritRating"] ?: 0.0) * 10.0
    }

    override fun blueSocketEp(deltas: Map<String, Double>): Double {
        // 5 agi
        return (deltas["agility"] ?: 0.0) * 5.0
    }
}
