package character.races

import character.Race
import character.Ability
import character.Buff
import character.Stats
import sim.SimIteration

class Tauren : Race() {
    override var baseStats: Stats = Stats(
        strength = 5,
        agility = -4,
        stamina = 1,
        intellect = -4,
        spirit = 2
    )
    override fun racialByName(name: String): Ability? = null

    val endurance = object : Buff() {
        override val name: String = "Endurance"
        override val durationMs: Int = -1
        override val hidden: Boolean = true

        override fun modifyStats(sim: SimIteration): Stats? {
            return Stats(healthMultiplier = 1.05)
        }
    }

    override fun buffs(sim: SimIteration): List<Buff> = listOf(endurance)
}
