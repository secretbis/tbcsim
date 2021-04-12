package character.classes.shaman.talents

import character.Proc
import character.Talent
import sim.SimIteration

class ElementalWeapons(currentRank: Int) : Talent(currentRank) {
    companion object {
        const val name: String = "Elemental Weapons"
    }

    override val name: String = Companion.name
    override val maxRank: Int = 3

    fun windfuryApMultiplier(): Double {
        return when(currentRank) {
            1 -> 1.13
            2 -> 1.26
            3 -> 1.4
            else -> 1.0
        }
    }

    fun flametongueDamageMultiplier(): Double {
        return 1.0 + 0.05 * currentRank
    }
}
