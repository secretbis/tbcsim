package character.classes.druid.talents

import character.Talent

/**
 *
 */
class Moonfury(currentRank: Int) : Talent(currentRank) {
    companion object {
        const val name = "Moonfury"
    }

    override val name: String = Companion.name
    override val maxRank: Int = 5

    fun increasedDamagePercent(): Double {
        return (0.02 * currentRank)
    }
}