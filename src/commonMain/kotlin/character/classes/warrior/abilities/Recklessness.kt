package character.classes.warrior.abilities

import character.Ability
import character.Buff
import character.Stats
import mechanics.Rating
import sim.SimParticipant

class Recklessness : Ability() {
    companion object {
        const val name = "Recklessness"
    }

    override val id: Int = 1719
    override val name: String = Companion.name
    override fun gcdMs(sp: SimParticipant): Int = sp.physicalGcd().toInt()
    override fun cooldownMs(sp: SimParticipant): Int = 30 * 60 * 1000

    val buff = object : Buff() {
        override val name: String = "Recklessness"
        override val durationMs: Int = 15000

        override fun modifyStats(sp: SimParticipant): Stats {
            return Stats(meleeCritRating = 100.0 * Rating.critPerPct)
        }
    }

    override fun cast(sp: SimParticipant) {
        sp.addBuff(buff)
    }
}
