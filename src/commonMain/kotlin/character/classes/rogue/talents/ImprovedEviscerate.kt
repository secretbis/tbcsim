package character.classes.rogue.talents

import character.Talent

class ImprovedEviscerate(currentRank: Int) : Talent(currentRank) {
    companion object {
        const val name = "Improved Eviscerate"
    }

    override val name: String = Companion.name
    override val maxRank: Int = 3

    fun damageIncreasePercent(): Double {
        return currentRank * 5.0
    }
}