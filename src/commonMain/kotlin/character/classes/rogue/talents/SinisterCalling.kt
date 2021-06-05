package character.classes.rogue.talents

import character.*

class SinisterCalling(currentRank: Int) : Talent(currentRank) {
    companion object {
        const val name = "Sinister Calling"
    }

    override val name: String = Companion.name
    override val maxRank: Int = 5

    // TODO: not implemented, not used in PvE anyways
}