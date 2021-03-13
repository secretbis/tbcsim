package data.abilities.raid

import character.Ability
import character.Buff
import character.Stats
import sim.SimParticipant

class GraceOfAirTotem : Ability() {
    companion object {
        const val name = "Grace of Air Totem"
    }

    override val id: Int = 25359
    override val name: String = Companion.name
    override fun gcdMs(sp: SimParticipant): Int = 0

    val buff = object : Buff() {
        override val name: String = "Grace of Air Totem"
        // Assume the caster is always maintaining this
        override val durationMs: Int = -1
        override val hidden: Boolean = true

        // Assume a GoA uptime of about 80% when twisting
        // Also assume the caster has Enhancing Totems
        override fun modifyStats(sp: SimParticipant): Stats? {
            return Stats(
                agility = (77.0 * 1.15 * 0.8).toInt()
            )
        }
    }

    override fun cast(sp: SimParticipant) {
        sp.addBuff(buff)
    }
}
