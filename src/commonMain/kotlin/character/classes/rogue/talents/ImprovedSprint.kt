package character.classes.rogue.talents

import character.*

class ImprovedSprint(currentRank: Int) : Talent(currentRank) {
    companion object {
        const val name = "Improved Sprint"
    }

    override val name: String = Companion.name
    override val maxRank: Int = 2
}