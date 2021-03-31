package data.abilities.raid

import character.Ability
import character.Buff
import character.Stats
import sim.SimParticipant

class BloodFrenzy : Ability() {
    companion object {
        const val name = "Blood Frenzy"
    }

    override val id: Int = 29859
    override val name: String = Companion.name
    override fun gcdMs(sp: SimParticipant): Int = 0

    val buff = object : Buff() {
        override val name: String = "Blood Frenzy"
        // Assume the caster is always maintaining this
        override val durationMs: Int = -1
        override val hidden: Boolean = true

        override fun modifyStats(sp: SimParticipant): Stats {
            return Stats(
                physicalDamageMultiplier = 1.04
            )
        }
    }

    override fun cast(sp: SimParticipant) {
        sp.sim.addRaidBuff(buff)
    }
}
