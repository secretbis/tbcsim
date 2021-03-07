package character.classes.warlock.talents

import character.Talent

class UnstableAffliction(currentRank: Int) : Talent(currentRank) {
    companion object {
        const val name = "Unstable Affliction"
    }
    override val name: String = Companion.name
    override val maxRank: Int = 1
}
