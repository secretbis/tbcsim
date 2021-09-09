package character.classes.priest.talents

import character.Talent

class ShadowPower(currentRank: Int) : Talent(currentRank) {
    companion object {
        const val name = "Shadow Power"
    }
    override val name: String = Companion.name
    override val maxRank: Int = 5

    fun critIncreasePct(): Double = 0.03 * currentRank
}
