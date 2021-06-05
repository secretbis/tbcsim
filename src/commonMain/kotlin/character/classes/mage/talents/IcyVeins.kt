package character.classes.mage.talents

import character.Talent

class IcyVeins(currentRank: Int) : Talent(currentRank) {
    companion object {
        const val name = "Icy Veins"
    }
    override val name: String = Companion.name
    override val maxRank: Int = 1
}
