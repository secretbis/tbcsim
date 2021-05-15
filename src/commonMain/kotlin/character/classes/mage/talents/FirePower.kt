package character.classes.mage.talents

import character.Buff
import character.Stats
import character.Talent
import sim.SimParticipant

class FirePower(currentRank: Int) : Talent(currentRank) {
    companion object {
        const val name = "Fire Power"
    }
    override val name: String = Companion.name
    override val maxRank: Int = 5

    val buff = object : Buff() {
        override val name: String = Companion.name
        override val durationMs: Int = -1
        override val hidden: Boolean = true

        override fun modifyStats(sp: SimParticipant): Stats {
            return Stats(fireDamageMultiplier = 1.0 + (0.02 * currentRank))
        }
    }

    override fun buffs(sp: SimParticipant): List<Buff> = listOf(buff)
}
