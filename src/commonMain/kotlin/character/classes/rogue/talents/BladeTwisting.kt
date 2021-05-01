package character.classes.rogue.talents

import character.*

class BladeTwisting(currentRank: Int) : Talent(currentRank) {
    companion object {
        const val name = "Blade Twisting"
    }

    override val name: String = Companion.name
    override val maxRank: Int = 1
}