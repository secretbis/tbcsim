package character.classes.priest.talents

import character.Talent

class FocusedPower(currentRank: Int) : Talent(currentRank) {
    companion object {
        const val name = "Focused Power"
    }
    override val name: String = Companion.name
    override val maxRank: Int = 2

    fun bonusSmiteMindBlastHitPct(): Double = 0.02 * currentRank
}
