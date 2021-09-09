package character.classes.priest.talents

import character.Talent
import character.Buff
import character.Stats
import sim.SimParticipant

class Shadowform(currentRank: Int) : Talent(currentRank) {
    companion object {
        const val name = "Shadowform"
    }
    override val name: String = Companion.name
    override val maxRank: Int = 1

    val buff = object : Buff() {
        override val name: String = Companion.name
        override val durationMs: Int = -1
        override val hidden: Boolean = true

        override fun modifyStats(sp: SimParticipant): Stats {
            return Stats(shadowDamageMultiplier = 1.0 + (0.15 * currentRank))
        }
    }

    override fun buffs(sp: SimParticipant): List<Buff> = listOf(buff)
}
