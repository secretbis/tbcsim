package character.classes.druid.talents

import character.Talent

/**
 *
 */
class FocusedStarlight(currentRank: Int) : Talent(currentRank) {
    companion object {
        const val name = "Focused Starlight"
    }

    override val name: String = Companion.name
    override val maxRank: Int = 2

    fun increasedWrathCritChancePercent() : Double {
        return currentRank * .02
    }

    fun increasedStarfireCritChancePercent() : Double {
        return increasedWrathCritChancePercent()
    }
}