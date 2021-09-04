package character.classes.priest.talents

import character.Talent

class FocusedMind(currentRank: Int) : Talent(currentRank) {
    companion object {
        const val name = "Focused Mind"
    }
    override val name: String = Companion.name
    override val maxRank: Int = 3

    fun manaReductionMultiplier(): Double = 1 - (0.05 * currentRank)
}
