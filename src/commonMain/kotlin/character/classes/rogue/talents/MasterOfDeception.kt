package character.classes.rogue.talents

import character.*

class MasterOfDeception(currentRank: Int) : Talent(currentRank) {
    companion object {
        const val name = "Master of Deception"
    }

    override val name: String = Companion.name
    override val maxRank: Int = 5
}