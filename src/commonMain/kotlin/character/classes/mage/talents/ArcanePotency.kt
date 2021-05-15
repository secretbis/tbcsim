package character.classes.mage.talents

import character.Talent

class ArcanePotency(currentRank: Int) : Talent(currentRank) {
    companion object {
        const val name = "Arcane Potency"
    }
    override val name: String = Companion.name
    override val maxRank: Int = 3

    fun addlClearcastingCritPct(): Double = 10.0 * currentRank
}
