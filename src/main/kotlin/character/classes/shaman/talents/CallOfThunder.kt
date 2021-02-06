package character.classes.shaman.talents

import character.Talent

class CallOfThunder(ranks: Int) : Talent(ranks) {
    companion object {
        const val name = "Call of Thunder"
    }

    override val name: String = Companion.name
    override val maxRank: Int = 5

    fun additionalLightningCritChance(): Double {
        return 0.01 * currentRank
    }
}
