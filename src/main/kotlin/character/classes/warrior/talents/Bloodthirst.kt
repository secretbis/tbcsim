package character.classes.warrior.talents

import character.Talent

class Bloodthirst(currentRank: Int) : Talent(currentRank) {
    companion object {
        const val name = "Bloodthirst"
    }

    override val name: String = Companion.name
    override val maxRank: Int = 1
}
