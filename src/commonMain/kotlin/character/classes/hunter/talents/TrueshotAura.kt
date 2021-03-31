package character.classes.hunter.talents

import character.Buff
import character.Stats
import character.Talent
import sim.SimParticipant

class TrueshotAura(currentRank: Int) : Talent(currentRank) {
    companion object {
        const val name = "Trueshot Aura"
    }
    override val name: String = Companion.name
    override val maxRank: Int = 1

    // Technically this is an ability, but that never matters
    // Just modeling as a static buff
    val buff = object : Buff() {
        override val name: String = Companion.name
        override val durationMs: Int = -1

        override fun modifyStats(sp: SimParticipant): Stats {
            return Stats(
                attackPower = 125,
                rangedAttackPower = 125
            )
        }
    }

    override fun buffs(sp: SimParticipant): List<Buff> = listOf(buff)
}
