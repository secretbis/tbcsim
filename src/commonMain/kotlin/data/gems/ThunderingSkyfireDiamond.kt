package data.gems

import character.Ability
import character.Buff
import character.Proc
import character.Stats
import data.model.Color
import data.model.Gem
import data.model.Item
import sim.Event
import sim.SimIteration

class ThunderingSkyfireDiamond : Gem(listOf(), Color.META, Quality.META) {

    val hasteBuff = object : Buff() {
        override val name: String = "Thundering Skyfire Diamond"
        override val durationMs: Int = 10000

        override fun modifyStats(sim: SimIteration): Stats {
            return Stats(physicalHasteRating = 240.0)
        }
    }

    val buff = object : Buff() {
        override val name: String = "Thundering Skyfire Diamond (passive)"
        override val durationMs: Int = -1
        override val hidden: Boolean = true

        override fun modifyStats(sim: SimIteration): Stats {
            return stats
        }

        val proc = object : Proc() {
            override val triggers: List<Trigger> = listOf(
                Trigger.MELEE_AUTO_HIT,
                Trigger.MELEE_AUTO_CRIT,
                Trigger.MELEE_WHITE_HIT,
                Trigger.MELEE_WHITE_CRIT,
                Trigger.MELEE_YELLOW_HIT,
                Trigger.MELEE_YELLOW_CRIT,
            )

            // https://70.wowfan.net/en/?spell=39958
            override val type: Type = Type.PPM
            override val ppm: Double = 0.7
            override fun cooldownMs(sim: SimIteration): Int = 40000

            override fun proc(sim: SimIteration, items: List<Item>?, ability: Ability?, event: Event?) {
                sim.addBuff(hasteBuff)
            }
        }

        override fun procs(sim: SimIteration): List<Proc> = listOf(proc)
    }

    override var buffs: List<Buff> = listOf(buff)
}