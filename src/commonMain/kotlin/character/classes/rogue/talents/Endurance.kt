package character.classes.rogue.talents

import character.*

class Endurance(currentRank: Int) : Talent(currentRank) {
    companion object {
        const val name = "Endurance"
    }

    override val name: String = Companion.name
    override val maxRank: Int = 2
}