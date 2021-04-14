package character.classes.shaman.specs

import character.Spec
import character.SpecEpDelta

class Enhancement : Spec() {
    override val name: String = "Enhancement"
    override val epBaseStat: SpecEpDelta = attackPowerBase
    // Enhance can theoretically make use of basically every stat
    override val epStatDeltas: List<SpecEpDelta> = dualWieldMeleeDeltas + casterHybridDeltas + spellPowerBase
    override val benefitsFromMeleeWeaponDps = true

    override fun redSocketEp(deltas: Map<String, Double>): Double {
        // 10 str
        return (deltas["strength"] ?: 0.0) * 10.0
    }

    override fun yellowSocketEp(deltas: Map<String, Double>): Double {
        // 5 crit rating / 5 str
        return ((deltas["meleeCritRating"] ?: 0.0) * 5.0) + ((deltas["strength"] ?: 0.0) * 5.0)
    }

    override fun blueSocketEp(deltas: Map<String, Double>): Double {
        // 5 str
        return (deltas["strength"] ?: 0.0) * 5.0
    }
}
