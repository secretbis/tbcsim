package character.classes.hunter.talents

import character.Buff
import character.Stats
import character.Talent
import sim.SimParticipant

class CombatExperience(currentRank: Int) : Talent(currentRank) {
    companion object {
        const val name = "Combat Experience"
    }
    override val name: String = Companion.name
    override val maxRank: Int = 2

    val buff = object : Buff() {
        override val name: String = Companion.name
        override val durationMs: Int = -1
        override val hidden: Boolean = true
        override val icon: String = "ability_hunter_combatexperience.jpg"

        override fun modifyStats(sp: SimParticipant): Stats {
            return Stats(
                agilityMultiplier = 1.0 + (0.01 * currentRank),
                intellectMultiplier = 1.0 + (0.03 * currentRank)
            )
        }
    }

    override fun buffs(sp: SimParticipant): List<Buff> = listOf(buff)
}
