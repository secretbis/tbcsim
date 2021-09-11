package data.abilities.raid

import character.*
import sim.SimParticipant

class Malediction : Ability() {
    companion object {
        const val name = "Malediction"
    }

    override val id: Int = 32484
    override val name: String = Companion.name
    override fun gcdMs(sp: SimParticipant): Int = 0

    val buff = object : Buff() {
        override val name: String = Companion.name
        override val durationMs: Int = -1
        override val hidden: Boolean = true
    }

    override fun cast(sp: SimParticipant) {
        sp.sim.addRaidBuff(buff)
    }
}
