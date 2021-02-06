package character.classes.shaman.talents

import character.Talent

class TotemicFocus(ranks: Int) : Talent(ranks) {
    companion object {
        const val name = "Totemic Focus"
    }

    override val name: String = Companion.name
    override val maxRank: Int = 5

    fun totemCostReduction(): Double {
        return 0.05 * currentRank
    }
}
