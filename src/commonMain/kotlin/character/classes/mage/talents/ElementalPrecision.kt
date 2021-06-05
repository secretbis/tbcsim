package character.classes.mage.talents

import character.Talent

class ElementalPrecision(currentRank: Int) : Talent(currentRank) {
    companion object {
        const val name = "Elemental Precision"
    }
    override val name: String = Companion.name
    override val maxRank: Int = 3

    fun bonusFireFrostHitPct(): Double = 0.01 * currentRank
}
