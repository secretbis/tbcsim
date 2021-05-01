package character.classes.rogue.talents

import character.Talent

class MasterPoisoner(currentRank: Int) : Talent(currentRank) {
    companion object {
        const val name = "Master Poisoner"
    }

    override val name: String = Companion.name
    override val maxRank: Int = 2

    fun additionalPoisonResistReduction: Double {
        return currentRank * 0.05
    }

    // TODO: implement once poisons can be resisted
}