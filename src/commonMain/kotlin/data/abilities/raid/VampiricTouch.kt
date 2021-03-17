package data.abilities.raid

import character.Ability
import character.Buff
import character.Stats
import sim.SimParticipant

class VampiricTouch(val dps: Int): Ability() {
    override val id: Int = 34917
    override val name: String = "Vampiric Touch ($dps DPS)"
    override fun gcdMs(sp: SimParticipant): Int = 0

    val buff = object : Buff() {
        override val name: String = "Vampiric Touch ($dps DPS)"
        // Assume the caster is always maintaining this
        override val durationMs: Int = -1
        override val hidden: Boolean = true

        // Assume a GoA uptime of about 80% when twisting
        // Also assume the caster has Enhancing Totems
        override fun modifyStats(sp: SimParticipant): Stats {
            return Stats(
                manaPer5Seconds = (0.05 * dps).toInt()
            )
        }
    }

    override fun cast(sp: SimParticipant) {
        sp.addBuff(buff)
    }
}
