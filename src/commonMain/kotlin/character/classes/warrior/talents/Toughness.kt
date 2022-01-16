package character.classes.warrior.talents

import character.Buff
import character.Stats
import character.Talent
import mechanics.Rating
import sim.SimParticipant

class Toughness(currentRank: Int) : Talent(currentRank) {
    companion object {
        const val name = "Toughness"
    }

    override val name: String = Companion.name
    override val maxRank: Int = 5

    val buff = object : Buff() {
        override val name: String = Companion.name
        override val icon: String = "spell_holy_devotion.jpg"
        override val durationMs: Int = -1
        override val hidden: Boolean = true

        override fun modifyStats(sp: SimParticipant): Stats {
            return Stats(
                armorMultiplier = 1.0 + (0.2 * currentRank)
            )
        }
    }

    override fun buffs(sp: SimParticipant): List<Buff> = listOf(buff)
}
