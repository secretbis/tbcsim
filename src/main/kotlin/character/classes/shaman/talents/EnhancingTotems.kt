package character.classes.shaman.talents

import character.Proc
import character.Talent
import sim.SimIteration

class EnhancingTotems(currentRank: Int) : Talent(currentRank) {
    companion object {
        const val name: String = "Enhancing Totems"
    }

    override val name: String = Companion.name
    override val maxRank: Int = 2

    // TODO: Apply this to SoE Totem
    fun strengthOfEarthMultiplier(): Double {
        return 1.0 + when(currentRank) {
            1 -> 0.08
            2 -> 0.15
            else -> 0.0
        }
    }

    // TODO: Apply this to GoA Totem
    fun graceOfAirTotemMultiplier(): Double {
        return 1.0 + when(currentRank) {
            1 -> 0.08
            2 -> 0.15
            else -> 0.0
        }
    }
}
