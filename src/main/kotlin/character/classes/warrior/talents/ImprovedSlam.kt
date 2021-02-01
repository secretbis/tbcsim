package character.classes.warrior.talents

import character.Talent

class ImprovedSlam(currentRank: Int) : Talent(currentRank) {
    companion object {
        const val name = "Improved Slam"
    }

    override val name: String = Companion.name
    override val maxRank: Int = 2
}
