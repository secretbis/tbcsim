package data.buffs

import character.Ability
import character.Buff
import character.Proc
import character.Stats
import data.model.Item
import sim.Event
import sim.SimParticipant

class TsunamiTalisman : Buff() {
    override val name: String = "Tsunami Talisman (static)"
    override val durationMs: Int = -1
    override val hidden: Boolean = true

    val proc = object : Proc() {
        override val triggers: List<Trigger> = listOf(
            Trigger.MELEE_AUTO_CRIT,
            Trigger.MELEE_WHITE_CRIT,
            Trigger.MELEE_YELLOW_CRIT
        )
        override val type: Type = Type.PERCENT
        override fun percentChance(sp: SimParticipant): Double = 10.0
        override fun cooldownMs(sp: SimParticipant): Int = 45000
        // proc rate value is in spell data, ICD based off EJ comments
        val buff = object : Buff() {
            override val id: Int = 42083
            override val name: String = "Tsunami Talisman"
            override val durationMs: Int = 10000

            override fun modifyStats(sp: SimParticipant): Stats? {
                return Stats(
                    attackPower = 340,
                    rangedAttackPower = 340
                )
            }
        }
        override fun proc(sp: SimParticipant, items: List<Item>?, ability: Ability?, event: Event?) {
            sp.addBuff(buff)
        }
    }

    override fun procs(sp: SimParticipant): List<Proc> = listOf(proc)
}
