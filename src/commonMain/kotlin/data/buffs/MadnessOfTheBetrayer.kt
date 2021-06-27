package data.buffs

import character.Ability
import character.Buff
import character.Proc
import character.Stats
import data.model.Item
import sim.Event
import sim.SimParticipant

class MadnessOfTheBetrayer : Buff() {
    override val name: String = "Madness of the Betrayer (static)"
    override val durationMs: Int = -1
    override val hidden: Boolean = true

    val proc = object : Proc() {
        override val triggers: List<Trigger> = listOf(
            Trigger.MELEE_AUTO_HIT,
            Trigger.MELEE_AUTO_CRIT,
            Trigger.MELEE_WHITE_HIT,
            Trigger.MELEE_WHITE_CRIT,
            Trigger.MELEE_YELLOW_HIT,
            Trigger.MELEE_YELLOW_CRIT,
            Trigger.MELEE_BLOCK,
            Trigger.MELEE_GLANCE,
            Trigger.RANGED_AUTO_HIT,
            Trigger.RANGED_AUTO_CRIT,
            Trigger.RANGED_WHITE_HIT,
            Trigger.RANGED_WHITE_CRIT,
            Trigger.RANGED_YELLOW_HIT,
            Trigger.RANGED_YELLOW_CRIT,
            Trigger.RANGED_BLOCK,
        )
        override val type: Type = Type.PPM
        override val ppm: Double = 1.0

        val buff = object : Buff() {
            override val id: Int = 40475
            override val name: String = "Madness of the Betrayer"
            override val durationMs: Int = 10000

            override fun modifyStats(sp: SimParticipant): Stats? {
                return Stats(
                    armorPen = 300
                )
            }
        }
        override fun proc(sp: SimParticipant, items: List<Item>?, ability: Ability?, event: Event?) {
            sp.addBuff(buff)
        }
    }

    override fun procs(sp: SimParticipant): List<Proc> = listOf(proc)
}
