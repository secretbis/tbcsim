package character.classes.warrior.specs

import character.Spec
import character.SpecEpDelta

class Arms : Spec() {
    override val name: String = "Arms"
    override val epBaseStat: SpecEpDelta = attackPowerBase
    override val epStatDeltas: List<SpecEpDelta> = defaultMeleeDeltas
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
