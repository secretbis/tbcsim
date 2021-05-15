package character.classes.mage.talents

import character.Talent

class ImprovedFrostbolt(currentRank: Int) : Talent(currentRank) {
    companion object {
        const val name = "Improved Frostbolt"
    }
    override val name: String = Companion.name
    override val maxRank: Int = 5

    fun frostboltCastTimeReductionMs(): Int = 100 * currentRank
}
