package character.classes.rogue.talents

import character.*

class SurpriseAttacks(currentRank: Int) : Talent(currentRank) {
    companion object {
        const val name = "Surprise Attacks"
    }

    override val name: String = Companion.name
    override val maxRank: Int = 1

    fun damageIncreasePercent(): Double {
        return currentRank * 10.0
    }
}