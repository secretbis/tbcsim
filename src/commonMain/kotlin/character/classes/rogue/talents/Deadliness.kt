package character.classes.rogue.talents

import character.*
import sim.SimParticipant

class Deadliness(currentRank: Int) : Talent(currentRank) {
    companion object {
        const val name = "Deadliness"
    }

    override val name: String = Companion.name
    override val maxRank: Int = 5

    fun attackPowerMultiplier(): Double {
        return 1.0 + (currentRank * 0.02)
    }

    val buff = object : Buff() {
        override val name: String = "${Companion.name} (Talent)"
        override val icon: String = "inv_weapon_crossbow_11.jpg"
        override val durationMs: Int = -1
        override val hidden: Boolean = true

        override fun modifyStats(sp: SimParticipant): Stats {
            return Stats(
                attackPowerMultiplier = attackPowerMultiplier()
            )
        }
    }

    override fun buffs(sp: SimParticipant): List<Buff> = listOf(buff)
}
