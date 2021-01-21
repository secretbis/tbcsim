package character.classes.shaman.talents

import character.Proc
import character.Talent

class ElementalWeapons(currentRank: Int) : Talent(currentRank) {
    companion object {
        const val name: String = "Elemental Weapons"
    }

    override val name: String = Companion.name
    override val maxRank: Int = 3

    override val procs: List<Proc> = listOf()

    fun windfuryApMultiplier(): Double {
        return when(currentRank) {
            0 -> 1.0
            1 -> 1.13
            2 -> 1.26
            else -> 1.4
        }
    }

    // TODO: Apply this to Flametongue Weapon
    fun flametongueDamageMultiplier(): Double {
        return 1.0 + 0.05 * currentRank
    }
}
