package character.classes.druid.talents

import character.Talent

/**
 *
 */
class StarlightWrath(currentRank: Int) : Talent(currentRank) {
    companion object {
        const val name = "Starlight Wrath"
    }

    override val name: String = Companion.name
    override val maxRank: Int = 5

    fun reducedWrathCooldownSeconds() : Double {
        return 0.1 * currentRank;
    }
}