package character.classes.mage.talents

import character.Buff
import character.Stats
import character.Talent
import mechanics.Rating
import sim.SimParticipant

class EmpoweredFrostbolt(currentRank: Int) : Talent(currentRank) {
    companion object {
        const val name = "Empowered Frostbolt"
    }
    override val name: String = Companion.name
    override val maxRank: Int = 5

    fun frostboltBonusSpellDamageMultiplier(): Double = 1.0 + (currentRank * 0.02)
    fun frostboltAddlCritPct(): Double = currentRank * 0.01
}
