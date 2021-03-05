package data.abilities.raid

import character.*
import sim.SimIteration

class CurseOfShadow : Ability() {
    companion object {
        const val name = "Curse of Shadow"
    }

    override val id: Int = 27226
    override val name: String = Companion.name
    override fun gcdMs(sim: SimIteration): Int = 0

    val buff = object : Buff() {
        override val name: String = "Curse of Shadow"
        // Assume the caster is always maintaining this
        override val durationMs: Int = -1
        override val hidden: Boolean = true

        override fun modifyStats(sim: SimIteration): Stats? {
            return Stats(
                shadowDamageMultiplier = 1.1
            )
        }
    }

    val debuff = object : Debuff() {
        override val name: String = "Curse of Shadow"
        // Assume the caster is always maintaining this
        override val durationMs: Int = -1
        override val hidden: Boolean = true

        override fun modifyStats(sim: SimIteration): Stats? {
            return Stats(
                shadowResistance = -88
            )
        }
    }

    override fun cast(sim: SimIteration) {
        sim.addBuff(buff)
        sim.addDebuff(debuff)
    }
}
