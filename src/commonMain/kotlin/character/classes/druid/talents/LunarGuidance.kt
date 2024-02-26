package character.classes.druid.talents

import character.Buff
import character.Stats
import character.Talent
import mechanics.Rating
import sim.SimParticipant

/**
 *
 */
class LunarGuidance(currentRank: Int) : Talent(currentRank) {
    companion object {
        const val name = "Lunar Guidance"
    }

    override val name: String = Companion.name
    override val maxRank: Int = 3

    fun increasedSpellDamagePercentByIntellect() : Double {
        return when(currentRank) {
            1 -> 0.08
            2 -> 0.16
            3 -> 0.25
            else -> 0.0
        }
    }

    val buff = object : Buff() {
        override val name: String = MoonkinForm.name
        override val durationMs: Int = -1
        override val hidden: Boolean = true

        override fun modifyStats(sp: SimParticipant): Stats {
            // TODO: is it easier to do this than add the 5% crit on each spell call?
            return Stats(spellDamage= (increasedSpellDamagePercentByIntellect() * sp.spellDamage()).toInt())
        }
    }

    override fun buffs(sp: SimParticipant): List<Buff> = listOf(buff)
}