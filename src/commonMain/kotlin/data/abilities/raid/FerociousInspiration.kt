package data.abilities.raid

import character.Ability
import character.Buff
import character.Stats
import sim.SimParticipant

class FerociousInspiration : Ability() {
    companion object {
        const val name = "Ferocious Inspiration"
    }

    override val id: Int = 34460
    override val name: String = Companion.name
    override fun gcdMs(sp: SimParticipant): Int = 0

    val buff = object : Buff() {
        override val name: String = "Ferocious Inspiration"
        // Assume the caster is always maintaining this
        override val durationMs: Int = -1
        override val hidden: Boolean = true

        // TODO: What's the typical uptime on this?  Currently assumes 100% uptime
        override fun modifyStats(sp: SimParticipant): Stats? {
            return Stats(
                physicalDamageMultiplier = 1.03,
                spellDamageMultiplier = 1.03
            )
        }
    }

    override fun cast(sp: SimParticipant) {
        sp.addBuff(buff)
    }
}
