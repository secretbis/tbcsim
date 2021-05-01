package character.classes.rogue.talents

import character.*

class MasterOfSubtlety(currentRank: Int) : Talent(currentRank) {
    companion object {
        const val name = "Master of Subtlety"
    }

    override val name: String = Companion.name
    override val maxRank: Int = 3

    // TODO: unimplemented, not used in PvE anyways
}