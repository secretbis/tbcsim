package character.classes.priest.talents

import character.Talent

class MindFlay(currentRank: Int) : Talent(currentRank) {
    companion object {
        const val name = "Mind Flay"
    }
    override val name: String = Companion.name
    override val maxRank: Int = 1
}
