package character.classes.hunter.talents

import character.Talent

class ImprovedBarrage(currentRank: Int) : Talent(currentRank) {
    companion object {
        const val name = "Improved Barrage"
    }
    override val name: String = Companion.name
    override val maxRank: Int = 3

    fun multiShotAdditionalCritChance(): Double {
        return 0.04 * currentRank
    }
}
