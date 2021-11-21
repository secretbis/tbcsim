package character.classes.rogue.talents

import character.*
import mechanics.Rating
import sim.SimParticipant

class Vitality(currentRank: Int) : Talent(currentRank) {
    companion object {
        const val name = "Vitality"
    }

    override val name: String = Companion.name
    override val maxRank: Int = 2

    fun agilityModifier(): Double {
        return 1.0 + (currentRank * 0.01)
    }

    fun staminaMultiplier(): Double {
        return 1.0 + (currentRank * 0.02)
    }

    val buff = object : Buff() {
        override val name: String = "${Companion.name} (Talent)"
        override val icon: String = "inv_helmet_21.jpg"
        override val durationMs: Int = -1
        override val hidden: Boolean = true

        override fun modifyStats(sp: SimParticipant): Stats {
            return Stats(
                agilityMultiplier = agilityModifier(),
                staminaMultiplier = staminaMultiplier()
            )
        }
    }

    override fun buffs(sp: SimParticipant): List<Buff> = listOf(buff)
}
