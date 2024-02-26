package character.classes.druid.talents

import character.Buff
import character.Stats
import character.Talent
import mechanics.General
import mechanics.Rating
import sim.SimParticipant

/**
 *
 */
class MoonkinForm(currentRank: Int) : Talent(currentRank) {
    companion object {
        const val name = "Moonkin Form"
    }

    override val name: String = Companion.name
    override val maxRank: Int = 1

    val buff = object : Buff() {
        override val name: String = Companion.name
        override val durationMs: Int = -1
        override val hidden: Boolean = true

        override fun modifyStats(sp: SimParticipant): Stats {
            // TODO: is it easier to do this than add the 5% crit on each spell call?
            return Stats(spellCritRating = 5 * Rating.critPerPct)
        }
    }

    override fun buffs(sp: SimParticipant): List<Buff> = listOf(buff)
}