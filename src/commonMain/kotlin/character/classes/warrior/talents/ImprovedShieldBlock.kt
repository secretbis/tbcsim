package character.classes.warrior.talents

import character.Talent

class ImprovedShieldBlock(currentRank: Int) : Talent(currentRank) {
    companion object {
        const val name = "Improved Shield Block"
    }

    override val name: String = Companion.name
    override val maxRank: Int = 1
}
