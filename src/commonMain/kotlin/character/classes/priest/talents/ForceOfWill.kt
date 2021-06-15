package character.classes.priest.talents

import character.Buff
import character.Stats
import character.Talent
import sim.SimParticipant

class ForceOfWill(currentRank: Int) : Talent(currentRank) {
    companion object {
        const val name = "Force of Will"
    }
    override val name: String = Companion.name
    override val maxRank: Int = 5

    val buff = object : Buff() {
        override val name: String = Companion.name
        override val durationMs: Int = -1
        override val hidden: Boolean = true

        override fun modifyStats(sp: SimParticipant): Stats {
            return Stats(spellDamage = (sp.spellDamage() * 0.01 * currentRank).toInt(), spellCritRating = (0.01 * currentRank))
        }
    }

    override fun buffs(sp: SimParticipant): List<Buff> = listOf(buff)
}
