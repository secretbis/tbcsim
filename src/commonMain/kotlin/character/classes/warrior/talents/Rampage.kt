package character.classes.warrior.talents

import character.Talent

class Rampage(currentRank: Int) : Talent(currentRank) {
    companion object {
        const val name = "Rampage"
    }

    override val name: String = Companion.name
    override val maxRank: Int = 1
}
