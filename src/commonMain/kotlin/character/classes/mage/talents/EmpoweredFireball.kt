package character.classes.mage.talents

import character.Talent

class EmpoweredFireball(currentRank: Int) : Talent(currentRank) {
    companion object {
        const val name = "Empowered Fireball"
    }
    override val name: String = Companion.name
    override val maxRank: Int = 5

    fun fireballAddlSpellDamageMultiplier(): Double = 1.0 + (currentRank * 0.03)
}
