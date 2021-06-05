package character.classes.mage.talents

import character.Talent

class Combustion(currentRank: Int) : Talent(currentRank) {
    companion object {
        const val name = "Combustion"
    }
    override val name: String = Companion.name
    override val maxRank: Int = 3
}
