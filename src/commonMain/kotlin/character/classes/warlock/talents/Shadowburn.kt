package character.classes.warlock.talents

import character.Talent

class Shadowburn(currentRank: Int) : Talent(currentRank) {
    companion object {
        const val name = "Shadowburn"
    }
    override val name: String = Companion.name
    override val maxRank: Int = 1
}
