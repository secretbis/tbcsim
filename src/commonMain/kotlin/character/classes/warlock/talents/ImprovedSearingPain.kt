package character.classes.warlock.talents

import character.Talent

class ImprovedSearingPain(currentRank: Int) : Talent(currentRank) {
    companion object {
        const val name = "Improved Searing Pain"
    }
    override val name: String = Companion.name
    override val maxRank: Int = 3

    fun searingPainAddlCritPct(): Double {
        return 0.04 * currentRank
    }
}
