package character.classes.priest.talents

import character.Talent
import character.Buff
import character.Stats
import sim.SimParticipant

class Darkness(currentRank: Int) : Talent(currentRank) {
    companion object {
        const val name = "Darkness"
    }
    override val name: String = Companion.name
    override val maxRank: Int = 5

    val buff = object : Buff() {
        override val name: String = Companion.name
        override val icon: String = "spell_shadow_twilight.jpg"
        override val durationMs: Int = -1
        override val hidden: Boolean = true

        override fun modifyStats(sp: SimParticipant): Stats {
            return Stats(shadowDamageMultiplier = 1.0 + (0.02 * currentRank))
        }
    }

    override fun buffs(sp: SimParticipant): List<Buff> = listOf(buff)
}
