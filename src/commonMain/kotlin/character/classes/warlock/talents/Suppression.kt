package character.classes.warlock.talents

import character.Talent

class Suppression(currentRank: Int) : Talent(currentRank) {
    companion object {
        const val name = "Suppression"
    }
    override val name: String = Companion.name
    override val maxRank: Int = 5
}
