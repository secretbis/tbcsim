package character.classes.rogue.talents

import character.Talent

class VilePoisons(currentRank: Int) : Talent(currentRank) {
    companion object {
        const val name = "Vile Poisons"
    }

    override val name: String = Companion.name
    override val maxRank: Int = 5

    fun damageIncreasePercentEnvenom(): Double {
        return currentRank * 4.0
    }

    fun damageIncreasePercentPoisons(): Double {
        return currentRank * 4.0
    }

    fun poisonResistChancePercent(): Double {
        return currentRank * 8.0
    }
}