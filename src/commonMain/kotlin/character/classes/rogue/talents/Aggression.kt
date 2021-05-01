package character.classes.rogue.talents

import character.*

class Aggression(currentRank: Int) : Talent(currentRank) {
    companion object {
        const val name = "Aggression"
    }

    override val name: String = Companion.name
    override val maxRank: Int = 3

    fun damageIncreasePercent(): Double {
        return currentRank * 2.0
    }
}