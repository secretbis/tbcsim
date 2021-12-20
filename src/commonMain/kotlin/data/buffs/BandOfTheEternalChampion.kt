package data.buffs

import character.Ability
import character.Buff
import character.Proc
import character.Stats
import data.model.Item
import sim.Event
import sim.SimParticipant

class BandOfTheEternalChampion : Buff() {
    companion object {
        const val name = "Band of the Eternal Champion"
    }

    override val name: String = "${Companion.name} (static)"
    override val durationMs: Int = -1
    override val hidden: Boolean = true
    override val icon: String = "inv_jewelry_ring_55.jpg"

    val proc = object : Proc() {
        override val triggers: List<Trigger> = listOf(
            Trigger.MELEE_AUTO_HIT,
            Trigger.MELEE_AUTO_CRIT,
            Trigger.MELEE_WHITE_HIT,
            Trigger.MELEE_WHITE_CRIT,
            Trigger.MELEE_YELLOW_HIT,
            Trigger.MELEE_YELLOW_CRIT,
            Trigger.RANGED_AUTO_HIT,
            Trigger.RANGED_AUTO_CRIT,
            Trigger.RANGED_WHITE_HIT,
            Trigger.RANGED_WHITE_CRIT,
            Trigger.RANGED_YELLOW_HIT,
            Trigger.RANGED_YELLOW_CRIT
        )
        override val type: Type = Type.PERCENT
        override fun percentChance(sp: SimParticipant): Double = 10.0
        override fun cooldownMs(sp: SimParticipant): Int = 60000

        val buff = object : Buff() {
            override val name: String = Companion.name
            override val durationMs: Int = 10000
            override val icon: String = "inv_jewelry_ring_55.jpg"

            override fun modifyStats(sp: SimParticipant): Stats {
                return Stats(
                    attackPower = 160,
                    rangedAttackPower = 160
                )
            }
        }

        override fun proc(sp: SimParticipant, items: List<Item>?, ability: Ability?, event: Event?) {
            sp.addBuff(buff)
        }

    }

    override fun procs(sp: SimParticipant): List<Proc> = listOf(proc)
}