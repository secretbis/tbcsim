package character.classes.rogue.specs

import character.Spec
import character.SpecEpDelta

class Assassination : Spec() {
    override val name: String = "Assassination"
    override val epBaseStat: SpecEpDelta = attackPowerBase
    override val epStatDeltas: List<SpecEpDelta> = defaultMeleeDeltas
    override val benefitsFromMeleeWeaponDps = true

    override fun redSocketEp(deltas: Map<String, Double>): Double {
        // 10 agi
        return (deltas["agility"] ?: 0.0) * 10.0
    }

    override fun yellowSocketEp(deltas: Map<String, Double>): Double {
        // 5 agi / 5 hit
        return (deltas["meleeHitRating"] ?: 0.0) * 5 + (deltas["agility"] ?: 0.0) * 5
    }

    override fun blueSocketEp(deltas: Map<String, Double>): Double {
        // 5 agi
        return (deltas["agility"] ?: 0.0) * 5.0
    }
}
