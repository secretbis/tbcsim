package character.classes.rogue.talents

import character.*

class HeightenedSenses(currentRank: Int) : Talent(currentRank) {
    companion object {
        const val name = "Heightened Senses"
    }

    override val name: String = Companion.name
    override val maxRank: Int = 2
}