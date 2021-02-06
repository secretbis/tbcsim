package character.classes.shaman.talents

import character.Talent

class TidalMastery(ranks: Int) : Talent(ranks) {
    companion object {
        const val name = "Tidal Mastery"
    }

    override val name: String = Companion.name
    override val maxRank: Int = 5

    fun additionalLightningAndHealingCritChance(): Double {
        return 0.01 * currentRank
    }
}
