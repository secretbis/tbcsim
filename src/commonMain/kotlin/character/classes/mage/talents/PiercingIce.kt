package character.classes.mage.talents

import character.Talent

class PiercingIce(currentRank: Int) : Talent(currentRank) {
    companion object {
        const val name = "Piercing Ice"
    }
    override val name: String = Companion.name
    override val maxRank: Int = 3

    fun frostDamageMultiplier(): Double = 1.0 + (currentRank * 0.01)
}
