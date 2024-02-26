package character.classes.druid.talents

import character.Buff
import character.Stats
import character.Talent
import mechanics.Rating
import sim.SimParticipant

/**
 *
 */
class BalanceOfPower(currentRank: Int) : Talent(currentRank) {
    companion object {
        const val name = "Balance of Power"
    }

    override val name: String = Companion.name
    override val maxRank: Int = 2

    val buff = object : Buff() {
        override val name: String = MoonkinForm.name
        override val durationMs: Int = -1
        override val hidden: Boolean = true

        override fun modifyStats(sp: SimParticipant): Stats {
            // TODO: is it easier to do this than add the 2% hit on each spell call?
            return Stats(spellHitRating=  currentRank * 2 * Rating.spellHitPerPct)
        }
    }

    override fun buffs(sp: SimParticipant): List<Buff> = listOf(buff)
}