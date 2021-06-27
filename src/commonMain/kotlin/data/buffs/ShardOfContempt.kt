package data.buffs

import character.Ability
import character.Buff
import character.Proc
import character.Stats
import data.model.Item
import sim.Event
import sim.SimParticipant

class ShardOfContempt : Buff() {
    override val name: String = "Shard of Contempt (static)"
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
        override val type: Type = Type.PERCENT
        override fun percentChance(sp: SimParticipant): Double = 10.0
        override fun cooldownMs(sp: SimParticipant): Int = 45000

        val buff = object : Buff() {
            override val id: Int = 45354
            override val name: String = "Shard of Contempt"
            override val durationMs: Int = 20000

            override fun modifyStats(sp: SimParticipant): Stats? {
                return Stats(
                    attackPower = 230,
                    rangedAttackPower = 230
                )
            }
        }
        override fun proc(sp: SimParticipant, items: List<Item>?, ability: Ability?, event: Event?) {
            sp.addBuff(buff)
        }
    }

    override fun procs(sp: SimParticipant): List<Proc> = listOf(proc)
}
