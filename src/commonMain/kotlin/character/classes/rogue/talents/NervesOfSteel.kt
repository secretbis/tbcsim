package character.classes.rogue.talents

import character.*

class NervesOfSteel(currentRank: Int) : Talent(currentRank) {
    companion object {
        const val name = "Nerves of Steel"
    }

    override val name: String = Companion.name
    override val maxRank: Int = 2
}