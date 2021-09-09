package character.classes.priest.talents

import character.Talent

class ImprovedMindBlast(currentRank: Int) : Talent(currentRank) {
    companion object {
        const val name = "Improved Mind Blast"
    }
    override val name: String = Companion.name
    override val maxRank: Int = 5

    fun cooldownReductionMs(): Int = 500 * currentRank
}
