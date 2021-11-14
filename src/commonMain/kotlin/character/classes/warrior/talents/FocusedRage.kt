package character.classes.warrior.talents

import character.Talent

class FocusedRage(currentRank: Int) : Talent(currentRank) {
    companion object {
        const val name = "Focused Rage"
    }

    override val name: String = Companion.name
    override val maxRank: Int = 3
}
