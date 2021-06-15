package character.classes.priest.talents

import character.Talent

class DivineFury(currentRank: Int) : Talent(currentRank) {
    companion object {
        const val name = "Divine Fury"
    }
    override val name: String = Companion.name
    override val maxRank: Int = 5

    fun smiteHolyFireCastTimeReductionMs(): Int = 100 * currentRank
}
