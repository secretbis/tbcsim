package character.classes.rogue.talents

import character.*

class ImprovedKick(currentRank: Int) : Talent(currentRank) {
    companion object {
        const val name = "Improved Kick"
    }

    override val name: String = Companion.name
    override val maxRank: Int = 2
}