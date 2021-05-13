package data.abilities.raid

import character.Ability
import character.Buff
import character.Stats
import mechanics.Rating
import sim.SimParticipant

class Bloodlust : Ability() {
    companion object {
        const val name = "Bloodlust"
    }

    override val id: Int = -1
    override val name: String = Companion.name
    override fun gcdMs(sp: SimParticipant): Int = 0

    val buff = object : Buff() {
        override val name: String = Companion.name
        override val durationMs: Int = 20000
        override val hidden: Boolean = true

        override fun modifyStats(sp: SimParticipant): Stats {
            val rating = 35.0 * Rating.hastePerPct
            return Stats(
                physicalHasteRating = rating,
                spellHasteRating = rating
            )
        }
    }

    override fun cast(sp: SimParticipant) {
        sp.sim.addRaidBuff(buff)
    }
}
