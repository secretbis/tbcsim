package data.abilities.raid

import character.Ability
import character.Buff
import character.Stats
import mechanics.Rating
import sim.SimParticipant

class InspiringPresence : Ability() {
    companion object {
        const val name = "Inspiring Presence"
    }

    override val id: Int = 28878
    override val name: String = Companion.name
    override val icon: String = "inv_staff_23.jpg"
    override fun gcdMs(sp: SimParticipant): Int = 0

    val buff = object : Buff() {
        override val name: String = Companion.name
        override val icon: String = "inv_staff_23.jpg"
        override val durationMs: Int = -1

        override fun modifyStats(sp: SimParticipant): Stats {
            return Stats(spellHitRating = 1.0 * Rating.spellHitPerPct)
        }
    }

    override fun cast(sp: SimParticipant) {
        sp.sim.addRaidBuff(buff)
    }
}
