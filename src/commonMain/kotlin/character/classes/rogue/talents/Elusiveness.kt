package character.classes.rogue.talents

import character.*

class Elusiveness(currentRank: Int) : Talent(currentRank) {
    companion object {
        const val name = "Elusiveness"
    }

    override val name: String = Companion.name
    override val maxRank: Int = 2

    fun cooldownReducedMs(): Int {
        return currentRank * 45000
    }
}