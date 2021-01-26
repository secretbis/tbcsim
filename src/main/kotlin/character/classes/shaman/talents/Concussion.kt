package character.classes.shaman.talents

import character.Proc
import character.Talent
import sim.SimIteration

class Concussion(currentRank: Int) : Talent(currentRank) {
    companion object {
        const val name: String = "Concussion"
    }

    override val name: String = Companion.name
    override val maxRank: Int = 5

    // TODO: Apply this to shocks and lightning spells
    fun shockAndLightningMultiplier(): Double {
        return 1.0 + (0.01 * currentRank)
    }
}
