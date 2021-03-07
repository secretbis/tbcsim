package character.classes.warlock.talents

import character.Talent

class SiphonLife(currentRank: Int) : Talent(currentRank) {
    companion object {
        const val name = "Siphon Life"
    }
    override val name: String = Companion.name
    override val maxRank: Int = 1
}
