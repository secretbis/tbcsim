package character.classes.rogue.talents

import character.*

class Riposte(currentRank: Int) : Talent(currentRank) {
    companion object {
        const val name = "Riposte"
    }

    override val name: String = Companion.name
    override val maxRank: Int = 1
}