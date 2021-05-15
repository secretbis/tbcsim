package character.classes.mage.talents

import character.Talent

class ImprovedFireball(currentRank: Int) : Talent(currentRank) {
    companion object {
        const val name = "Improved Fireball"
    }
    override val name: String = Companion.name
    override val maxRank: Int = 5

    fun fireballCastTimeReductionMs(): Int = currentRank * 100
}
