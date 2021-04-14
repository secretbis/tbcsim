package data.abilities.raid

import character.Ability
import character.Buff
import character.Stats
import mechanics.Rating
import sim.SimParticipant

class LeaderOfThePack : Ability() {
    companion object {
        const val name = "Leader of the Pack"
    }

    override val id: Int = 17007
    override val name: String = Companion.name
    override fun gcdMs(sp: SimParticipant): Int = 0

    val buff = object : Buff() {
        override val name: String = "Leader of the Pack"
        // Assume the caster is always maintaining this
        override val durationMs: Int = -1
        override val hidden: Boolean = true

        val bonusCritRating = 5.0 * Rating.critPerPct
        override fun modifyStats(sp: SimParticipant): Stats {
            return Stats(
                meleeCritRating = bonusCritRating,
                rangedCritRating = bonusCritRating
            )
        }
    }

    override fun cast(sp: SimParticipant) {
        sp.sim.addRaidBuff(buff)
    }
}
