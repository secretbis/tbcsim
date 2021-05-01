package character.classes.rogue.talents

import character.*

class SleightOfHand(currentRank: Int) : Talent(currentRank) {
    companion object {
        const val name = "Sleight of Hand"
    }

    override val name: String = Companion.name
    override val maxRank: Int = 2
}