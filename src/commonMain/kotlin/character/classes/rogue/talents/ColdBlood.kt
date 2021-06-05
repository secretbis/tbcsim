package character.classes.rogue.talents

import character.*

class ColdBlood(currentRank: Int) : Talent(currentRank) {
    companion object {
        const val name = "Cold Blood"
    }

    override val name: String = Companion.name
    override val maxRank: Int = 1
}