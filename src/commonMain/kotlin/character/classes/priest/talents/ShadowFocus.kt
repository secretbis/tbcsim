package character.classes.priest.talents

import character.Talent

class ShadowFocus(currentRank: Int) : Talent(currentRank) {
    companion object {
        const val name = "Shadow Focus"
    }

    override val name: String = Companion.name
    override val maxRank: Int = 5

    fun shadowHitIncreasePct(): Double = 0.02 * currentRank
}
