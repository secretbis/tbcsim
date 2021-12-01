package character.classes.hunter.talents

import character.*
import data.model.Item
import mechanics.Rating
import sim.Event
import sim.SimParticipant

class ImprovedAspectOfTheHawk(currentRank: Int) : Talent(currentRank) {
    companion object {
        const val name = "Improved Aspect of the Hawk"
    }

    override val name: String = Companion.name
    override val maxRank: Int = 5
}
