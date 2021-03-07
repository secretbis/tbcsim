package character.classes.warlock.talents

import character.Talent

class Conflagrate(currentRank: Int) : Talent(currentRank) {
    companion object {
        const val name = "Conflagrate"
    }
    override val name: String = Companion.name
    override val maxRank: Int = 1
}
