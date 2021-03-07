package character.classes.warlock.talents

import character.Talent

class Bane(currentRank: Int) : Talent(currentRank) {
    companion object {
        const val name = "Bane"
    }
    override val name: String = Companion.name
    override val maxRank: Int = 5

    fun destructionCastReductionAmountMs(): Int {
        return 100 * currentRank
    }
}
