package character.classes.rogue.talents

import character.*

class Preparation(currentRank: Int) : Talent(currentRank) {
    companion object {
        const val name = "Preparation"
    }

    override val name: String = Companion.name
    override val maxRank: Int = 1
}