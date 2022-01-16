package character.classes.warrior.talents

import character.*
import sim.SimParticipant

class ShieldMastery(currentRank: Int) : Talent(currentRank) {
    companion object {
        const val name = "Shield Mastery"
    }

    override val name: String = Companion.name
    override val maxRank: Int = 3

    val buff = object : Buff() {
        override val name: String = Companion.name
        override val icon: String = "ability_warrior_shieldmastery.jpg"
        override val durationMs: Int = -1
        override val hidden: Boolean = true

        override fun modifyStats(sp: SimParticipant): Stats {
            return Stats(
                blockValueMultiplier = 1.0 + (0.1 * currentRank)
            )
        }
    }

    override fun buffs(sp: SimParticipant): List<Buff> = listOf(buff)
}
