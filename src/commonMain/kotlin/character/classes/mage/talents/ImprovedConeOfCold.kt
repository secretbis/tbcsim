package character.classes.mage.talents

import character.Talent

class ImprovedConeOfCold(currentRank: Int) : Talent(currentRank) {
    companion object {
        const val name = "Improved Cone of Cold"
    }
    override val name: String = Companion.name
    override val maxRank: Int = 3

    fun coneOfColdDamageMultiplier(): Double = 1.0 + (currentRank * 0.15)
}
