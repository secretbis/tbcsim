package character.classes.warlock.talents

import character.Buff
import character.Stats
import character.Talent
import sim.SimIteration

class Emberstorm(currentRank: Int) : Talent(currentRank) {
    companion object {
        const val name = "Emberstorm"
    }
    override val name: String = Companion.name
    override val maxRank: Int = 5

    val fireBuff = object : Buff() {
        override val name: String = "Emberstorm"
        override val durationMs: Int = -1
        override val hidden: Boolean = true

        override fun modifyStats(sim: SimIteration): Stats {
            return Stats(fireDamageMultiplier = 1.0 + (0.02 * currentRank))
        }
    }

    override fun buffs(sim: SimIteration): List<Buff> = listOf(fireBuff)

    fun incinerateCastTimeMultiplier(): Double {
        return 1.0 - 0.02 * currentRank
    }

    fun fireDamageMultiplier(): Double {
        return 1.0 + (0.02 * currentRank)
    }
}
