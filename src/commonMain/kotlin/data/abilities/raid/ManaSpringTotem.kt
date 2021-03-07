package data.abilities.raid

import character.Ability
import character.Buff
import character.Stats
import sim.SimIteration

class ManaSpringTotem : Ability() {
    companion object {
        const val name = "Mana Spring Totem"
    }

    override val id: Int = 30706
    override val name: String = Companion.name
    override fun gcdMs(sim: SimIteration): Int = 0

    val buff = object : Buff() {
        override val name: String = Companion.name
        // Assume the caster is always maintaining this
        override val durationMs: Int = -1
        override val hidden: Boolean = true

        // Assume a GoA uptime of about 80% when twisting
        // Also assume the caster has Enhancing Totems
        override fun modifyStats(sim: SimIteration): Stats? {
            return Stats(
                manaPer5Seconds = 50
            )
        }
    }

    override fun cast(sim: SimIteration) {
        sim.addBuff(buff)
    }
}
