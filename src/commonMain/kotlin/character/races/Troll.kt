package character.races

import character.Race
import character.Ability
import character.Buff
import character.Stats
import character.races.abilities.Berserking
import mechanics.Rating
import sim.SimParticipant

class Troll : Race() {
    override var baseStats: Stats = Stats(
        strength = 1,
        agility = 2,
        intellect = -4,
        spirit = 1
    )

    override fun racialByName(name: String): Ability? {
        return when(name) {
            "Berserking" -> Berserking()
            else -> null
        }
    }

    // TODO: Beast slaying racial, and target types in general
    val bowSpec = object : Buff() {
        override val name: String = "Bow Specialization"
        override val durationMs: Int = -1
        override val hidden: Boolean = true

        override fun modifyStats(sp: SimParticipant): Stats {
            return Stats(bowCritRating = 1.0 * Rating.critPerPct)
        }
    }

    override fun buffs(sp: SimParticipant): List<Buff> = listOf(bowSpec)
}
