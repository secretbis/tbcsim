package character.classes.hunter.talents

import character.Buff
import character.Stats
import character.Talent
import sim.SimParticipant

class MortalShots(currentRank: Int) : Talent(currentRank) {
    companion object {
        const val name = "Mortal Shots"
    }
    override val name: String = Companion.name
    override val maxRank: Int = 2

    val buff = object : Buff() {
        override val name: String = Companion.name
        override val durationMs: Int = -1
        override val hidden: Boolean = true

        override fun modifyStats(sp: SimParticipant): Stats {
            val critMult = 1.0 + (0.03 * currentRank)
            return Stats(
                whiteDamageAddlCritMultiplier = critMult,
                yellowDamageAddlCritMultiplier = critMult
            )
        }
    }

    override fun buffs(sp: SimParticipant): List<Buff> = listOf(buff)
}
