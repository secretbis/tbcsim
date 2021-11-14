package character.classes.warrior.talents

import character.Talent

class Devastate(currentRank: Int) : Talent(currentRank) {
    companion object {
        const val name = "Devastate"
    }
    override val name: String = Companion.name
    override val maxRank: Int = 1
}
