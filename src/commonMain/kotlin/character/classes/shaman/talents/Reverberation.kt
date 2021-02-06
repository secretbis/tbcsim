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

    fun shockCooldownReductionAmountMs(): Int {
        return (0.2 * currentRank * 1000).toInt()
    }
}
