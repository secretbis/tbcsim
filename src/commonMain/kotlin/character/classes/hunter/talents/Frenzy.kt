package character.classes.hunter.talents

import character.Talent

class Frenzy(currentRank: Int) : Talent(currentRank) {
    companion object {
        const val name = "Frenzy"
    }
    override val name: String = Companion.name
    override val maxRank: Int = 5

    fun petFrenzyProcChancePct(): Double = 0.2 * currentRank
}
