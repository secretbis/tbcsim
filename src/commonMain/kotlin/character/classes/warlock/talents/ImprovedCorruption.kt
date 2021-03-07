package character.classes.warlock.talents

import character.Talent

class ImprovedCorruption(currentRank: Int) : Talent(currentRank) {
    companion object {
        const val name = "Improved Corruption"
    }
    override val name: String = Companion.name
    override val maxRank: Int = 5

    fun corruptionCastTimeReductionMs(): Int {
        return currentRank * 400
    }
}
