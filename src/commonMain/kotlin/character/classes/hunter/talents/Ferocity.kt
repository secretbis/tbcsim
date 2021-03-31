package character.classes.hunter.talents

import character.Talent

class Ferocity(currentRank: Int) : Talent(currentRank) {
    companion object {
        const val name = "Ferocity"
    }
    override val name: String = Companion.name
    override val maxRank: Int = 5

    fun petAdditionalCritPct(): Double = currentRank * 2.0
}
