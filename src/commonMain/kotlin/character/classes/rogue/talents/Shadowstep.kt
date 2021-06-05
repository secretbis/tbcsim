package character.classes.rogue.talents

import character.*

class Shadowstep(currentRank: Int) : Talent(currentRank) {
    companion object {
        const val name = "Shadowstep"
    }

    override val name: String = Companion.name
    override val maxRank: Int = 1

    // TODO: not implemented, not used in PvE anyways
}