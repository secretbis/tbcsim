package character.classes.rogue.talents

import character.*

class Deflection(currentRank: Int) : Talent(currentRank) {
    companion object {
        const val name = "Deflection"
    }

    override val name: String = Companion.name
    override val maxRank: Int = 5
}