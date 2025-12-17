package character.classes.hunter.talents

import character.Talent

class Frenzy(currentRank: Int) : Talent(currentRank) {
    companion object {
        const val name = "Frenzy"
    }

    override val name: String = Companion.name
    override val maxRank: Int = 5

    fun petFrenzyProcChancePct(): Double = 20.0 * currentRank
}
