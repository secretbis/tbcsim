package character.classes.warrior.talents

import character.Talent

class ImprovedThunderClap(currentRank: Int) : Talent(currentRank) {
    companion object {
        const val name = "Improved Thunder Clap"
    }

    override val name: String = Companion.name
    override val maxRank: Int = 3
}
