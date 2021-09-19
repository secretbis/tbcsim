package character.classes.druid.talents

import character.Talent

/**
 *
 */
class ImprovedMoonfire(currentRank: Int) : Talent(currentRank) {
    companion object {
        const val name = "Improved Moonfire"
    }

    override val name: String = Companion.name
    override val maxRank: Int = 2

    fun increasedMoonfireCritChance() : Double {
        return 10.0 * currentRank
    }

    fun increasedMoonfireDamage() : Double {
        return increasedMoonfireCritChance()
    }
}