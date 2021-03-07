package character.classes.warlock.talents

import character.Talent

class Devastation(currentRank: Int) : Talent(currentRank) {
    companion object {
        const val name = "Devastation"
    }
    override val name: String = Companion.name
    override val maxRank: Int = 5

    fun additionalDestructionCritChance(): Double {
        return 0.01 * currentRank
    }
}
