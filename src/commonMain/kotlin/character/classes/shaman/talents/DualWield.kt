package character.classes.shaman.talents

import character.Talent

class DualWield(currentRank: Int) : Talent(currentRank) {
    companion object {
        const val name = "Dual Wield"
    }

    override val name: String = Companion.name
    override val maxRank: Int = 1
}
