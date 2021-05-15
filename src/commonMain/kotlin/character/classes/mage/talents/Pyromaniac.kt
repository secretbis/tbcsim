package character.classes.mage.talents

import character.Talent

class Pyromaniac(currentRank: Int) : Talent(currentRank) {
    companion object {
        const val name = "Pyromaniac"
    }
    override val name: String = Companion.name
    override val maxRank: Int = 3

    fun fireSpellAddlCritPct(): Double = 1.0 * currentRank
    fun fireSpellManaCostReduction(): Double = 1.0 * currentRank
}
