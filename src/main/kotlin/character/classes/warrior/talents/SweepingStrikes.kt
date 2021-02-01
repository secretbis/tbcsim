package character.classes.warrior.talents

import character.Talent

class SweepingStrikes(currentRank: Int) : Talent(currentRank) {
    companion object {
        const val name = "Sweeping Strikes"
    }

    override val name: String = Companion.name
    override val maxRank: Int = 1
}
