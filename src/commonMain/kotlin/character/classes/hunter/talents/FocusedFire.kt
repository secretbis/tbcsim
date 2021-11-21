package character.classes.hunter.talents

import character.Buff
import character.Stats
import character.Talent
import sim.SimParticipant

class FocusedFire(currentRank: Int) : Talent(currentRank) {
    companion object {
        const val name = "Focused Fire"
    }
    override val name: String = Companion.name
    override val maxRank: Int = 2

    val buff = object : Buff() {
        override val name: String = Companion.name
        override val durationMs: Int = -1
        override val hidden: Boolean = true
        override val icon: String = "ability_hunter_silenthunter.jpg"

        override fun modifyStats(sp: SimParticipant): Stats {
            return Stats(physicalDamageMultiplier = 1.0 + (0.01 * currentRank))
        }
    }

    override fun buffs(sp: SimParticipant): List<Buff> = listOf(buff)

    fun additionalKillCommandCritChance(): Double {
        return 0.10 * currentRank
    }
}
