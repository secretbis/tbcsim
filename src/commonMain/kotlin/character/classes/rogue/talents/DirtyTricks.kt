package character.classes.rogue.talents

import character.*

class DirtyTricks(currentRank: Int) : Talent(currentRank) {
    companion object {
        const val name = "Dirty Tricks"
    }

    override val name: String = Companion.name
    override val maxRank: Int = 2
}