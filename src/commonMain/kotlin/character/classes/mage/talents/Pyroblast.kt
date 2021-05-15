package character.classes.mage.talents

import character.Talent

class Pyroblast(currentRank: Int) : Talent(currentRank) {
    companion object {
        const val name = "Pyroblast"
    }
    override val name: String = Companion.name
    override val maxRank: Int = 1
}
