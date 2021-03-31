package data.abilities.raid

import character.*
import mechanics.Rating
import sim.SimParticipant

class MoonkinAura : Ability() {
    companion object {
        const val name = "Moonkin Aura"
    }

    override val id: Int = 24907
    override val name: String = Companion.name
    override fun gcdMs(sp: SimParticipant): Int = 0

    val buff = object : Buff() {
        override val name: String = Companion.name
        override val durationMs: Int = -1
        override val hidden: Boolean = true

        override fun modifyStats(sp: SimParticipant): Stats {
            return Stats(spellCritRating = Rating.critPerPct * 5.0)
        }
    }

    override fun cast(sp: SimParticipant) {
        sp.sim.addRaidBuff(buff)
    }
}
