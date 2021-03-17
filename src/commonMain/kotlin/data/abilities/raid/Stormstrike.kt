package data.abilities.raid

import character.*
import sim.SimParticipant

class Stormstrike : Ability() {
    companion object {
        const val name = "Stormstrike"
    }

    override val id: Int = 17364
    override val name: String = Companion.name
    override fun gcdMs(sp: SimParticipant): Int = 0

    val buff = object : Buff() {
        override val name: String = "Stormstrike"
        // Assume the caster is always maintaining this
        override val durationMs: Int = -1
        override val hidden: Boolean = true

        override fun modifyStats(sp: SimParticipant): Stats {
            return Stats(natureDamageMultiplier = 1.2)
        }
    }

    override fun cast(sp: SimParticipant) {
        sp.addBuff(buff)
    }
}
