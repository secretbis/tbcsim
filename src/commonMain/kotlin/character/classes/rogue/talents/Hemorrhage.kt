package character.classes.rogue.talents

import character.*

class Hemorrhage(currentRank: Int) : Talent(currentRank) {
    companion object {
        const val name = "Hemorrhage"
    }

    override val name: String = Companion.name
    override val maxRank: Int = 1
}