package character.classes.hunter.talents

import character.Talent

class Barrage(currentRank: Int) : Talent(currentRank) {
    companion object {
        const val name = "Barrage"
    }
    override val name: String = Companion.name
    override val maxRank: Int = 3

    fun multiShotDamageMultiplier(): Double {
        return 1.0 + (0.04 * currentRank)
    }
}
