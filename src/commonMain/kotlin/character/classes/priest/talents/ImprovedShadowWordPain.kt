package character.classes.priest.talents

import character.Talent

class ImprovedShadowWordPain(currentRank: Int) : Talent(currentRank) {
    companion object {
        const val name = "Improved Shadow Word: Pain"
    }
    override val name: String = Companion.name
    override val maxRank: Int = 2

    fun swpDurationIncrease(): Int = 3000 * currentRank
}
