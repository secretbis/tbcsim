package character.classes.hunter.talents

import character.*
import data.model.Item
import mechanics.Rating
import sim.Event
import sim.SimParticipant

class MasterTactician(currentRank: Int) : Talent(currentRank) {
    companion object {
        const val name = "Master Tactician"
    }

    override val name: String = Companion.name
    override val maxRank: Int = 5

    val buff = object : Buff() {
        override val name: String = "${Companion.name} (static)"
        override val durationMs: Int = -1
        override val hidden: Boolean = true

        val critBuff = object : Buff() {
            override val name: String = Companion.name
            override val durationMs: Int = 8000

            override fun modifyStats(sp: SimParticipant): Stats {
                return Stats(
                    meleeCritRating = 2.0 * currentRank * Rating.critPerPct,
                    rangedCritRating = 2.0 * currentRank * Rating.critPerPct
                )
            }
        }

        val proc = object : Proc() {
            override val triggers: List<Trigger> = listOf(
                Trigger.RANGED_AUTO_HIT,
                Trigger.RANGED_AUTO_CRIT,
                Trigger.RANGED_WHITE_HIT,
                Trigger.RANGED_WHITE_CRIT,
                Trigger.RANGED_YELLOW_HIT,
                Trigger.RANGED_YELLOW_CRIT,
                Trigger.RANGED_BLOCK
            )
            override val type: Type = Type.PERCENT
            override fun percentChance(sp: SimParticipant): Double = 10.0

            override fun proc(sp: SimParticipant, items: List<Item>?, ability: Ability?, event: Event?) {
                sp.addBuff(critBuff)
            }
        }

        override fun procs(sp: SimParticipant): List<Proc> = listOf(proc)
    }

    override fun buffs(sp: SimParticipant): List<Buff> = listOf(buff)
}
