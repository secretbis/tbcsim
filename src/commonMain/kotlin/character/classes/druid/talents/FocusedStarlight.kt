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

    fun increasedWrathCritChance() : Double {
        return currentRank * 2.0
    }

    fun increasedStarfireCritChance() : Double {
        return increasedWrathCritChance()
    }
}