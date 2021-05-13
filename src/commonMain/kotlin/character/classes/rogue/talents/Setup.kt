package character.classes.rogue.talents

import character.*

class Setup(currentRank: Int) : Talent(currentRank) {
    companion object {
        const val name = "Setup"
    }

    override val name: String = Companion.name
    override val maxRank: Int = 3
}