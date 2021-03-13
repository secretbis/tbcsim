package character.races

import character.Ability
import character.Buff
import character.Race
import character.Stats
import sim.SimParticipant

class Gnome : Race() {
    override var baseStats: Stats = Stats(
        strength = -5,
        agility = 2,
        stamina = 0,
        intellect = 3,
        spirit = 0
    )

    val expansiveMind = object : Buff() {
        override val name: String = "Expansive Mind"
        override val durationMs: Int = -1
        override val hidden: Boolean = true

        override fun modifyStats(sp: SimParticipant): Stats? {
            return Stats(intellectMultiplier = 1.05)
        }
    }

    override fun racialByName(name: String): Ability? = null
    override fun buffs(sp: SimParticipant): List<Buff> = listOf(expansiveMind)
}
