package character.classes.druid.talents

import character.Talent

/**
 *
 */
class WrathOfCenarius(currentRank: Int) : Talent(currentRank) {
    companion object {
        const val name = "Wrath of Cenarius"
    }

    override val name: String = Dreamstate.name
    override val maxRank: Int = 5

    fun increasedSpellfireDamagePercent() : Double {
        return .04 * currentRank
    }

    fun increasedWrathDamagePercent() : Double {
        return .02 * currentRank
    }
}