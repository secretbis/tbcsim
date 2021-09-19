package character.classes.druid.talents

import character.Talent

/**
 *
 */
class LunarGuidance(currentRank: Int) : Talent(currentRank) {
    companion object {
        const val name = "Lunar Guidance"
    }

    override val name: String = Companion.name
    override val maxRank: Int = 3

    fun increasedSpellDamagePercentByIntellect() : Double {
        return when(currentRank) {
            1 -> 8.0
            2 -> 16.0
            3 -> 25.0
            else -> 0.0
        }
    }
}