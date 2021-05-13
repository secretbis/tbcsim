package character.classes.rogue.talents

import character.*

class AdrenalineRush(currentRank: Int) : Talent(currentRank) {
    companion object {
        const val name = "Adrenaline Rush"
    }

    override val name: String = Companion.name
    override val maxRank: Int = 1
}