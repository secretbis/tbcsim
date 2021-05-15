package character.classes.mage.talents

import character.Talent

class CriticalMass(currentRank: Int) : Talent(currentRank) {
    companion object {
        const val name = "Critical Mass"
    }
    override val name: String = Companion.name
    override val maxRank: Int = 3

    fun fireSpellAddlCritPct(): Double = 2.0 * currentRank
}
