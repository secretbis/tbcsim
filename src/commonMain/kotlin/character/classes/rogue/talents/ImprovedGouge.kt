package character.classes.rogue.talents

import character.*

class ImprovedGouge(currentRank: Int) : Talent(currentRank) {
    companion object {
        const val name = "Improved Gouge"
    }

    override val name: String = Companion.name
    override val maxRank: Int = 3
}