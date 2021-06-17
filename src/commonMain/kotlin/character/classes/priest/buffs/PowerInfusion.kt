package character.classes.priest.buffs

import character.Buff
import character.Stats
import mechanics.Rating
import sim.SimParticipant

class PowerInfusion : Buff() {
    companion object {
        const val name = "Power Infusion"
    }

    override val name: String = Companion.name
    override val durationMs: Int = 15000

    fun manaCostMultiplier(): Double = 0.8
    fun spellHasteMultiplier(): Double = 0.8

    override fun modifyStats(sp: SimParticipant): Stats {
        return Stats(spellHasteRating = 20.0 * Rating.hastePerPct)
    }
}
