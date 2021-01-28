package data.gems

import character.Buff
import character.Proc
import character.Stats
import data.model.Color
import data.model.Gem
import data.model.Socket
import sim.SimIteration

class ChaoticSkyfireDiamond : Gem(listOf(), Color.META, Quality.META) {
    val buff = object : Buff() {
        override val name: String = "Chaotic Skyfire Diamond"
        override val durationMs: Int = -1
        override val hidden: Boolean = true

        override fun modifyStats(sim: SimIteration, stats: Stats): Stats {
            return stats.add(Stats(
                physicalCritRating = 12.0,
                whiteDamageAddlCritMultiplier = 1.03,
                yellowDamageAddlCritMultiplier = 1.03
            ))
        }

        override fun procs(sim: SimIteration): List<Proc> = listOf()
    }

    override var buffs: List<Buff> = listOf(buff)

    override fun metaActive(sockets: List<Socket>): Boolean {
        val byColor = socketsByColor(sockets)

        // Most meta gems have a requirement of at least 2 of each color
        // Ones that do not will override this
        return byColor[Color.BLUE] ?: 0 >= 2
    }
}
