package data.buffs

import character.Ability
import character.Buff
import character.Proc
import character.Stats
import data.model.Item
import sim.Event
import sim.SimIteration

class DragonspineTrophy : Buff() {
    override val id: Int = 34774
    override val name: String = "Dragonspine Trophy"
    override val durationMs: Int = -1

    val proc = object : Proc() {
        override val triggers: List<Trigger> = listOf(
            Trigger.MELEE_AUTO_HIT,
            Trigger.MELEE_AUTO_CRIT,
            Trigger.MELEE_WHITE_HIT,
            Trigger.MELEE_WHITE_CRIT,
            Trigger.MELEE_YELLOW_HIT,
            Trigger.MELEE_YELLOW_CRIT
        )

        override val type: Type = Type.PPM
        override val ppm: Double = 1.0

        val buff = object : Buff() {
            override val name: String = "Dragonspine Trophy"
            override val durationMs: Int = 10000

            override fun modifyStats(sim: SimIteration): Stats {
                return Stats(physicalHasteRating = 325.0)
            }
        }

        override fun proc(sim: SimIteration, items: List<Item>?, ability: Ability?, event: Event?) {
            sim.addBuff(buff)
        }
    }

    override fun procs(sim: SimIteration): List<Proc> = listOf(proc)
}
