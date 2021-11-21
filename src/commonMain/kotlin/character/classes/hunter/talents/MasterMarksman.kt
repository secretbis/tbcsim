package character.classes.hunter.talents

import character.Buff
import character.Stats
import character.Talent
import sim.SimParticipant

class MasterMarksman(currentRank: Int) : Talent(currentRank) {
    companion object {
        const val name = "Master Marksman"
    }
    override val name: String = Companion.name
    override val maxRank: Int = 5

    val buff = object : Buff() {
        override val name: String = Companion.name
        override val durationMs: Int = -1
        override val hidden: Boolean = true
        override val icon: String = "ability_hunter_mastermarksman.jpg"

        override fun modifyStats(sp: SimParticipant): Stats {
            return Stats(
                rangedAttackPowerMultiplier = 1.0 + (0.02 * currentRank)
            )
        }
    }

    override fun buffs(sp: SimParticipant): List<Buff> = listOf(buff)
}
