package character.races

import character.Ability
import character.Buff
import character.Race
import character.Stats
import mechanics.Rating
import sim.SimParticipant

class Dwarf : Race() {
    override var baseStats: Stats = Stats(
        strength = 5,
        agility = -4,
        stamina = 1,
        intellect = -1,
        spirit = -1
    )

    val gunSpec = object : Buff() {
        override val name: String = "Gun Specialization"
        override val durationMs: Int = -1
        override val hidden: Boolean = true

        override fun modifyStats(sp: SimParticipant): Stats? {
            return Stats(gunCritRating = 1.0 * Rating.critPerPct)
        }
    }

    override fun racialByName(name: String): Ability? = null
    override fun buffs(sp: SimParticipant): List<Buff> = listOf(gunSpec)
}
