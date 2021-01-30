package character.classes.shaman.talents

import character.Proc
import character.Talent
import sim.SimIteration

class ImprovedWeaponTotems(currentRank: Int) : Talent(currentRank) {
    companion object {
        const val name: String = "Improved Weapon Totems"
    }

    override val name: String = Companion.name
    override val maxRank: Int = 2

    fun windfuryTotemApMultiplier(): Double {
        return 1.0 + 0.15 * currentRank
    }

    // TODO: Apply this to Flametongue Totem
    fun flametongueTotemDamageMultiplier(): Double {
        return 1.0 + 0.06 * currentRank
    }
}
