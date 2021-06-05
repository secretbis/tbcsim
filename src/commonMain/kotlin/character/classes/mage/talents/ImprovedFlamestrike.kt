package character.classes.mage.talents

import character.Talent

class ImprovedFlamestrike(currentRank: Int) : Talent(currentRank) {
    companion object {
        const val name = "Improved Flamestrike"
    }
    override val name: String = Companion.name
    override val maxRank: Int = 3

    fun flamestrikeAddlCritPct() = 5.0 * currentRank
}
