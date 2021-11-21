package character.classes.mage.talents

import character.Buff
import character.Stats
import character.Talent
import mechanics.Rating
import sim.SimParticipant

class ArcaneInstability(currentRank: Int) : Talent(currentRank) {
    companion object {
        const val name = "Arcane Instability"
    }
    override val name: String = Companion.name
    override val maxRank: Int = 3

    val buff = object : Buff() {
        override val name: String = Companion.name
        override val durationMs: Int = -1
        override val hidden: Boolean = true
        override val icon: String = "spell_shadow_teleport.jpg"

        override fun modifyStats(sp: SimParticipant): Stats {
            return Stats(
                spellCritRating = currentRank * Rating.critPerPct,
                spellDamageMultiplier = 1.0 + (currentRank * 0.01)
            )
        }
    }

    override fun buffs(sp: SimParticipant): List<Buff> = listOf(buff)
}
