package character.classes.druid.talents

import character.Talent

/**
 *
 */
class Vengeance(currentRank: Int) : Talent(currentRank) {
    companion object {
        const val name = "Vengeance"
    }

    override val name: String = Companion.name
    override val maxRank: Int = 5

    fun increasedCritBonusDamagePercent() : Double {
        return 0.2 * currentRank
    }
}