package character.classes.rogue.talents

import character.*

class Premeditation(currentRank: Int) : Talent(currentRank) {
    companion object {
        const val name = "Premeditation"
    }

    override val name: String = Companion.name
    override val maxRank: Int = 1

    // TODO: not implemented, not used in PvE anyways
}