package character.classes.mage.talents

import character.Talent

class ArcaneFocus(currentRank: Int) : Talent(currentRank) {
    companion object {
        const val name = "Arcane Focus"
    }
    override val name: String = Companion.name
    override val maxRank: Int = 5

    fun arcaneAddlSpellHitPct(): Double = 0.02 * currentRank
}
