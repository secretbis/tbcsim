package character.classes.mage.talents

import character.Talent

class ArcanePower(currentRank: Int) : Talent(currentRank) {
    companion object {
        const val name = "Arcane Power"
    }
    override val name: String = Companion.name
    override val maxRank: Int = 1
}
