package character.classes.hunter.talents

import character.*
import data.model.Item
import sim.Event
import sim.SimParticipant

class ExposeWeakness(currentRank: Int) : Talent(currentRank) {
    companion object {
        const val name = "Expose Weakness"
    }

    override val name: String = Companion.name
    override val maxRank: Int = 3

    val buff = object : Buff() {
        override val name: String = "${Companion.name} (static)"
        override val durationMs: Int = -1
        override val hidden: Boolean = true

        val apBuff = object : Buff() {
            override val name: String = Companion.name
            override val durationMs: Int = 7000

            override val mutex: List<Mutex> = listOf(Mutex.BUFF_EXPOSE_WEAKNESS)
            override fun mutexPriority(sp: SimParticipant): Map<Mutex, Int> {
                return mapOf(
                    Mutex.BUFF_EXPOSE_WEAKNESS to sp.agility()
                )
            }

            override fun modifyStats(sp: SimParticipant): Stats {
                val totalAp = (0.25 * sp.agility()).toInt()
                return Stats(
                    attackPower = totalAp,
                    rangedAttackPower = totalAp
                )
            }
        }

        val proc = object : Proc() {
            override val triggers: List<Trigger> = listOf(
                Trigger.RANGED_AUTO_CRIT,
                Trigger.RANGED_WHITE_CRIT,
                Trigger.RANGED_YELLOW_CRIT
            )
            override val type: Type = Type.PERCENT
            override fun percentChance(sp: SimParticipant): Double = 100.0 * (currentRank / 3.0)

            override fun proc(sp: SimParticipant, items: List<Item>?, ability: Ability?, event: Event?) {
                sp.sim.addRaidBuff(apBuff)
            }
        }

        override fun procs(sp: SimParticipant): List<Proc> = listOf(proc)
    }

    override fun buffs(sp: SimParticipant): List<Buff> = listOf(buff)
}
