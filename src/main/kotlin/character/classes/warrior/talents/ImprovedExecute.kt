package character.classes.warrior.talents

import character.Talent

class ImprovedExecute(currentRank: Int) : Talent(currentRank) {
    companion object {
        const val name = "Improved Execute"
    }

    override val name: String = Companion.name
    override val maxRank: Int = 2
}
