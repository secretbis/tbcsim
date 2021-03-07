package character.classes.warlock.talents

import character.Talent

class Malediction(currentRank: Int) : Talent(currentRank) {
    companion object {
        const val name = "Malediction"
    }
    override val name: String = Companion.name
    override val maxRank: Int = 3
}
