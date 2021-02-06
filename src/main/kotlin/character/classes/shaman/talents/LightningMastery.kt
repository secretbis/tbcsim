package character.classes.shaman.talents

import character.Talent

class LightningMastery(currentRank: Int) : Talent(currentRank) {
    companion object {
        const val name = "Lightning Mastery"
    }

    override val name: String = Companion.name
    override val maxRank: Int = 5

    fun lightningCastReductionAmountMs(): Int {
        return 100 * currentRank
    }
}
