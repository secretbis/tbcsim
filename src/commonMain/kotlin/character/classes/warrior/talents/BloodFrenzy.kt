package character.classes.warrior.talents

import character.Talent

class BloodFrenzy(currentRank: Int) : Talent(currentRank) {
    companion object {
        const val name = "Blood Frenzy"
    }

    override val name: String = Companion.name
    override val maxRank: Int = 2
}
