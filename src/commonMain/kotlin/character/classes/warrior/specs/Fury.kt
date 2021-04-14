package character.classes.warrior.specs

import character.Spec
import character.SpecEpDelta

class Fury : Spec() {
    override val name: String = "Fury"
    override val epBaseStat: SpecEpDelta = attackPowerBase
    override val epStatDeltas: List<SpecEpDelta> = dualWieldMeleeDeltas
    override val benefitsFromMeleeWeaponDps = true

    override fun redSocketEp(deltas: Map<String, Double>): Double {
        // 10 str
        return (deltas["strength"] ?: 0.0) * 10.0
    }

    override fun yellowSocketEp(deltas: Map<String, Double>): Double {
        // 10 crit rating
        return (deltas["meleeCritRating"] ?: 0.0) * 10.0
    }

    override fun blueSocketEp(deltas: Map<String, Double>): Double {
        // 5 str
        return (deltas["strength"] ?: 0.0) * 5.0
    }
}
