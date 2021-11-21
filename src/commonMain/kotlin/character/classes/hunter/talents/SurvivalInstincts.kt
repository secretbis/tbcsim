package character.classes.hunter.talents

import character.Buff
import character.CharacterType
import character.Stats
import character.Talent
import mechanics.Rating
import sim.SimParticipant

class SurvivalInstincts(currentRank: Int) : Talent(currentRank) {
    companion object {
        const val name = "Survival Instincts"
    }
    override val name: String = Companion.name
    override val maxRank: Int = 2

    val buff = object : Buff() {
        override val name: String = Companion.name
        override val durationMs: Int = -1
        override val hidden: Boolean = true
        override val icon: String = "ability_hunter_survivalinstincts.jpg"

        override fun modifyStats(sp: SimParticipant): Stats {
            return Stats(
                rangedAttackPowerMultiplier = 1.0 + (currentRank * 0.02),
                attackPowerMultiplier = 1.0 + (currentRank * 0.02)
            )
        }
    }

    override fun buffs(sp: SimParticipant): List<Buff> = listOf(buff)
}
