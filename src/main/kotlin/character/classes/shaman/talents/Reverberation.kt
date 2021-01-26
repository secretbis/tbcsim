package character.classes.shaman.talents

import character.Proc
import character.Talent
import sim.SimIteration

class Reverberation(currentRank: Int) : Talent(currentRank) {
    companion object {
        const val name: String = "Reverberation"
    }

    override val name: String = Companion.name
    override val maxRank: Int = 5

    // TODO: Apply this to Shocks
    fun shockCooldownReductionAmount(): Double {
        return 0.2 * currentRank
    }
}
