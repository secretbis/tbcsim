package data.gems

import character.Buff
import character.Proc
import character.Stats
import data.model.Color
import data.model.Gem
import sim.SimIteration

class RelentlessEarthstormDiamond : Gem(listOf(), Color.META, Quality.META) {
    val buff = object : Buff() {
        override val name: String = "Relentless Earthstorm Diamond"
        override val durationMs: Int = -1
        override val hidden: Boolean = true

        override fun modifyStats(sim: SimIteration, stats: Stats): Stats {
            return stats.add(Stats(
                agility = 12,
                whiteDamageAddlCritMultiplier = 1.03,
                yellowDamageAddlCritMultiplier = 1.03
            ))
        }

        override fun procs(sim: SimIteration): List<Proc> = listOf()
    }

    override var buffs: List<Buff> = listOf(buff)
}
