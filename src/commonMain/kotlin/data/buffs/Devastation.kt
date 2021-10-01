package data.buffs

import character.*
import data.model.Item
import mechanics.Rating
import sim.Event
import sim.SimParticipant

class Devastation(val sourceItem: Item) : ItemBuff(listOf(sourceItem)) {
    override val id: Int = 36479
    override val name: String = "Devastation (static)"
    override val durationMs: Int = -1
    override val hidden: Boolean = true

    val proc = object : ItemProc(listOf(sourceItem)) {
        override val triggers: List<Trigger> = listOf(
            Trigger.MELEE_AUTO_HIT,
            Trigger.MELEE_AUTO_CRIT,
            Trigger.MELEE_WHITE_HIT,
            Trigger.MELEE_WHITE_CRIT,
            Trigger.MELEE_YELLOW_HIT,
            Trigger.MELEE_YELLOW_CRIT,
            Trigger.MELEE_BLOCK,
            Trigger.MELEE_GLANCE,
        )

        // No source for this, but most things are 1PPM or so
        override val type: Type = Type.PPM
        override val ppm: Double = 1.0

        val buff = object : Buff() {
            override val name: String = "Devastation"
            override val durationMs: Int = 30000

            override fun modifyStats(sp: SimParticipant): Stats {
                return Stats(physicalHasteRating = Rating.hastePerPct * 20)
            }
        }

        override fun proc(sp: SimParticipant, items: List<Item>?, ability: Ability?, event: Event?) {
            sp.addBuff(buff)
        }
    }

    override fun procs(sp: SimParticipant): List<Proc> = listOf(proc)
}
