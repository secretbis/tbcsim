package data.buffs

import character.*
import data.model.Item
import sim.Event
import sim.SimParticipant

class TheNightBlade(val sourceItem: Item) : ItemBuff(listOf(sourceItem)) {
    override val id: Int = 38307
    override val name: String = "The Night Blade (static)"
    override val durationMs: Int = -1
    override val hidden: Boolean = true

    val buff = object : Buff(){
        override val name: String = "The Night Blade"
        override val icon: String = "inv_weapon_shortblade_26.jpg"
        override val durationMs: Int =  10000
        override val maxStacks: Int = 3

        override fun modifyStats(sp: SimParticipant): Stats {
            val stacks = state(sp).currentStacks
            return Stats(
                armorPen = 435 * stacks
            )
        }

    }

    val stackProc = object : ItemProc(listOf(sourceItem)) {
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
        override val type: Type = Type.PPM
        override val ppm: Double = 2.0

        override fun proc(sp: SimParticipant, items: List<Item>?, ability: Ability?, event: Event?) {
            sp.addBuff(buff)
        }
    }

    override fun procs(sp: SimParticipant): List<Proc> = listOf(stackProc)
}
