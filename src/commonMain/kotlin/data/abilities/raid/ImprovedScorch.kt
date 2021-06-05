package data.abilities.raid

import character.*
import sim.SimParticipant

class ImprovedScorch : Ability() {
    companion object {
        const val name = "Improved Scorch"
    }

    override val id: Int = 12873
    override val name: String = Companion.name
    override fun gcdMs(sp: SimParticipant): Int = 0

    val buff = object : Buff() {
        override val name: String = "Improved Scorch"
        // Assume the caster is always maintaining this
        override val durationMs: Int = -1
        override val maxStacks: Int = 5

        override fun modifyStats(sp: SimParticipant): Stats {
            return Stats(fireDamageMultiplier = 1.15)
        }
    }

    override fun cast(sp: SimParticipant) {
        sp.sim.addRaidBuff(buff)
    }
}
