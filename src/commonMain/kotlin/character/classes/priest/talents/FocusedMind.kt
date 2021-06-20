package character.classes.priest.talents

import character.Talent

class FocusedMind(currentRank: Int) : Talent(currentRank) {
    companion object {
        const val name = "Focused Mind"
    }
    override val name: String = Companion.name
    override val maxRank: Int = 3

    fun mindBlastFlayManaCostReductionMultiplier(): Double = 1 - currentRank * 0.05
}
