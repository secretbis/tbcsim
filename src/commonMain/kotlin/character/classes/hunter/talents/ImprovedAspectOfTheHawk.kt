package character.classes.hunter.talents

import character.*
import data.model.Item
import mechanics.Rating
import sim.Event
import sim.SimParticipant

class ImprovedAspectOfTheHawk(currentRank: Int) : Talent(currentRank) {
    companion object {
        const val name = "Improved Aspect of the Hawk"
    }

    override val name: String = Companion.name
    override val maxRank: Int = 5

    val buff = object : Buff() {
        override val name: String = "${Companion.name} (static)"
        override val durationMs: Int = -1
        override val hidden: Boolean = true

        val hasteBuff = object : Buff() {
            override val name: String = Companion.name
            override val durationMs: Int = 12000

            override fun modifyStats(sp: SimParticipant): Stats {
                return Stats(physicalHasteRating = 3.0 * currentRank * Rating.hastePerPct)
            }
        }

        val proc = object : Proc() {
            override val triggers: List<Trigger> = listOf(
                Trigger.RANGED_AUTO_HIT,
                Trigger.RANGED_AUTO_CRIT
            )
            override val type: Type = Type.PERCENT
            override fun percentChance(sp: SimParticipant): Double = 10.0

            override fun proc(sp: SimParticipant, items: List<Item>?, ability: Ability?, event: Event?) {
                sp.addBuff(hasteBuff)
            }
        }

        override fun procs(sp: SimParticipant): List<Proc> = listOf(proc)
    }

    override fun buffs(sp: SimParticipant): List<Buff> = listOf(buff)
}
