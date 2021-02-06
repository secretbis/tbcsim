package character.races

import character.Ability
import character.Buff
import character.Race
import character.Stats
import mechanics.Rating
import sim.SimIteration

class NightElf : Race() {
    override var baseStats: Stats = Stats(
        strength = -4,
        agility = 4,
        stamina = 0,
        intellect = 0,
        spirit = 0
    )

    val dodge = object : Buff() {
        override val name: String = "Quickness"
        override val durationMs: Int = -1
        override val hidden: Boolean = true

        override fun modifyStats(sim: SimIteration): Stats {
            return Stats(
                dodgeRating = 1.0 * Rating.dodgePerPct
            )
        }
    }

    override fun racialByName(name: String): Ability? = null
    override fun buffs(sim: SimIteration): List<Buff> = listOf(dodge)
}
