package character.classes.rogue.talents

import character.*

class Mutilate(currentRank: Int) : Talent(currentRank) {
    companion object {
        const val name = "Mutilate"
    }

    override val name: String = Companion.name
    override val maxRank: Int = 1
}