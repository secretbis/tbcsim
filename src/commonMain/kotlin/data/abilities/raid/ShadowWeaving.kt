package data.abilities.raid

import character.*
import sim.SimParticipant

class ShadowWeaving : Ability() {
    companion object {
        const val name = "Shadow Weaving"
    }

    override val id: Int = 33195
    override val name: String = Companion.name
    override fun gcdMs(sp: SimParticipant): Int = 0

    val buff = object : Buff() {
        override val name: String = Companion.name
        // Assume the caster is always maintaining this at max stacks
        override val durationMs: Int = -1
        override val hidden: Boolean = true

        override fun modifyStats(sp: SimParticipant): Stats {
            return Stats(shadowDamageMultiplier = 1.1)
        }
    }

    override fun cast(sp: SimParticipant) {
        sp.sim.addRaidBuff(buff)
    }
}
