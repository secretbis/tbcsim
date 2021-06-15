package character.classes.priest.talents

import character.Buff
import character.Stats
import character.Talent
import sim.SimParticipant

class HolySpecialization(currentRank: Int) : Talent(currentRank) {
    companion object {
        const val name = "Holy Specialization"
    }
    override val name: String = Companion.name
    override val maxRank: Int = 5

    fun holySpellsCrit(): Double = 0.01 * currentRank
}
