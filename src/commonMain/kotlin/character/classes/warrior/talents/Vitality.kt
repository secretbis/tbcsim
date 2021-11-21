package character.classes.warrior.talents

import character.Buff
import character.Stats
import character.Talent
import sim.SimParticipant

class Vitality(currentRank: Int) : Talent(currentRank) {
    companion object {
        const val name = "Vitality"
    }

    override val name: String = Companion.name
    override val maxRank: Int = 5

    val buff = object : Buff() {
        override val name: String = Companion.name
        override val icon: String = "inv_helmet_21.jpg"
        override val durationMs: Int = -1
        override val hidden: Boolean = true

        override fun modifyStats(sp: SimParticipant): Stats {
            return Stats(
                strengthMultiplier = 1.0 + (currentRank * 0.02),
                staminaMultiplier = 1.0 +  + (currentRank * 0.01)
            )
        }
    }

    override fun buffs(sp: SimParticipant): List<Buff> = listOf(buff)
}
