package character.classes.priest.talents

import character.Talent

class InnerFocus(currentRank: Int) : Talent(currentRank) {
    companion object {
        const val name = "Inner Focus"
    }
    override val name: String = Companion.name
    override val maxRank: Int = 1
}
