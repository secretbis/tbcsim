package character.classes.rogue.talents

import character.*

class ImprovedAmbush(currentRank: Int) : Talent(currentRank) {
    companion object {
        const val name = "Improved Ambush"
    }

    override val name: String = Companion.name
    override val maxRank: Int = 3

    fun bonusCritChance(): Double {
        return currentRank * 0.15
    }
}