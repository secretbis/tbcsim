package character.classes.warlock.talents

import character.Buff
import character.Stats
import character.Talent
import mechanics.Rating
import sim.SimParticipant

class Backlash(currentRank: Int) : Talent(currentRank) {
    companion object {
        const val name = "Backlash"
    }
    override val name: String = Companion.name
    override val maxRank: Int = 3

    val critBuff = object : Buff() {
        override val name: String = "Backlash"
        override val icon: String = "spell_fire_playingwithfire.jpg"
        override val durationMs: Int = -1
        override val hidden: Boolean = true

        override fun modifyStats(sp: SimParticipant): Stats? {
            val rating = currentRank * Rating.critPerPct
            return Stats(spellCritRating = rating)
        }
    }

    override fun buffs(sp: SimParticipant): List<Buff> = listOf(critBuff)
}
