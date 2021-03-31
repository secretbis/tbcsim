package character.classes.hunter.talents

import character.Talent

class Efficiency(currentRank: Int) : Talent(currentRank) {
    companion object {
        const val name = "Efficiency"
    }
    override val name: String = Companion.name
    override val maxRank: Int = 5

    fun shotManaCostReduction(): Double {
        return 0.02 * currentRank
    }
}
