package character.classes.rogue.talents

import character.Talent

class PuncturingWounds(currentRank: Int) : Talent(currentRank) {
    companion object {
        const val name = "Puncturing Wounds"
    }

    override val name: String = Companion.name
    override val maxRank: Int = 3

    fun additionalCritChanceBackstab(): Double {
        return currentRank * 0.1
    }

    fun additionalCritChanceMutilate(): Double {
        return currentRank * 0.05
    }
}