package character.classes.mage.talents

import character.Talent

class ArcticWinds(currentRank: Int) : Talent(currentRank) {
    companion object {
        const val name = "Arctic Winds"
    }
    override val name: String = Companion.name
    override val maxRank: Int = 5

    fun frostDamageMultiplier(): Double = 1.0 + (currentRank * 0.01)
}
