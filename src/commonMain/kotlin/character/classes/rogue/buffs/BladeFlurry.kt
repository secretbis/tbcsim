package character.classes.rogue.buffs

import character.*
import character.classes.rogue.abilities.*
import data.Constants
import sim.Event
import sim.SimParticipant
import data.model.Item
import mechanics.Rating

class BladeFlurry() : Buff() {
    companion object {
        const val name = "Blade Flurry"
    }

    override val name: String = Companion.name
    override val durationMs: Int = 15000

    val hastePercent = 20.0

    override fun modifyStats(sp: SimParticipant): Stats {  
        val hasteRating = Rating.hastePerPct * hastePercent
        return Stats(
            physicalHasteRating = hasteRating
        )
    }
}