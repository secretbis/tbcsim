package character.classes.mage.talents

import character.Talent

class EmpoweredArcaneMissiles(currentRank: Int) : Talent(currentRank) {
    companion object {
        const val name = "Empowered Arcane Missiles"
    }
    override val name: String = Companion.name
    override val maxRank: Int = 3

    fun arcaneMissilesAddlSpellDamageMultiplier(): Double = 1.0 + (currentRank * 0.15)
    fun arcaneMissilesAddlManaCostMultiplier(): Double = 1.0 + (currentRank * 0.02)
}
