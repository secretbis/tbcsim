package data.abilities.raid

import character.Ability
import character.Buff
import character.Proc
import character.Stats
import sim.SimIteration

class FerociousInspiration : Ability() {
    companion object {
        const val name = "Ferocious Inspiration"
    }

    override val id: Int = 34460
    override val name: String = Companion.name
    override fun gcdMs(sim: SimIteration): Int = 0

    val buff = object : Buff() {
        override val name: String = Companion.name
        // Assume the caster is always maintaining this
        override val durationMs: Int = -1
        override val hidden: Boolean = true

        // TODO: What's the typical uptime on this?  Currently assumes 100% uptime
        override fun modifyStats(sim: SimIteration): Stats? {
            return Stats(
                physicalDamageMultiplier = 1.03,
                spellDamageMultiplier = 1.03
            )
        }
    }

    override fun cast(sim: SimIteration, free: Boolean) {
        sim.addBuff(buff)
    }
}
