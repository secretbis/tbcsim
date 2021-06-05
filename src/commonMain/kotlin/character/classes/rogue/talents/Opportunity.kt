package character.classes.rogue.talents

import character.*

class Opportunity(currentRank: Int) : Talent(currentRank) {
    companion object {
        const val name = "Opportunity"
    }

    override val name: String = Companion.name
    override val maxRank: Int = 5

    fun damageIncreasePercent(): Double {
        return currentRank * 4.0
    }
}