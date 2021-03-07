package character.classes.warlock.talents

import character.Talent

class Nightfall(currentRank: Int) : Talent(currentRank) {
    companion object {
        const val name = "Nightfall"
    }
    override val name: String = Companion.name
    override val maxRank: Int = 2
}
