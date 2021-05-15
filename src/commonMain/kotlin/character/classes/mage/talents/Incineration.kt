package character.classes.mage.talents

import character.Talent

class Incineration(currentRank: Int) : Talent(currentRank) {
    companion object {
        const val name = "Incineration"
    }
    override val name: String = Companion.name
    override val maxRank: Int = 2

    fun fireBlastScorchAddlCritPct(): Double = 2.0 * currentRank
}
