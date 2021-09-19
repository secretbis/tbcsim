package character.classes.druid.talents

import character.Talent

/**
 *
 */
class Moonglow(currentRank: Int) : Talent(currentRank) {
    companion object {
        const val name = "Moonglow"
    }

    override val name: String = Companion.name
    override val maxRank: Int = 3

    fun reducedManaCostPercent(): Double {
        return (0.03 * currentRank)
    }
}