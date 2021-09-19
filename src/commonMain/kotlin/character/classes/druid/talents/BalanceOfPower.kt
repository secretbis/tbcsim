package character.classes.druid.talents

import character.Talent

/**
 *
 */
class BalanceOfPower(currentRank: Int) : Talent(currentRank) {
    companion object {
        const val name = "Balance of Power"
    }

    override val name: String = Companion.name
    override val maxRank: Int = 2

    fun bonusSpellHitPercent(): Double {
        return (0.02 * currentRank)
    }
}