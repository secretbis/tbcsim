package character.classes.druid.talents

import character.Talent

/**
 *
 */
class NaturesReach(currentRank: Int) : Talent(currentRank) {
    companion object {
        const val name = "Nature's Reach"
    }

    override val name: String = Companion.name
    override val maxRank: Int = 2

    fun increasedBalanceSpellRangePercent() : Double {
        return 10.0 * currentRank
    }
}