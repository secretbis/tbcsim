package data.abilities.raid

import character.*
import sim.SimIteration

class CurseOfTheElements : Ability() {
    companion object {
        const val name = "Curse of the Elements"
    }

    override val id: Int = 27228
    override val name: String = Companion.name
    override fun gcdMs(sim: SimIteration): Int = 0

    val buff = object : Buff() {
        override val name: String = Companion.name
        // Assume the caster is always maintaining this
        override val durationMs: Int = -1
        override val hidden: Boolean = true

        override fun modifyStats(sim: SimIteration): Stats? {
            return Stats(
                fireDamageMultiplier = 1.1,
                frostDamageMultiplier = 1.1,
            )
        }
    }

    val debuff = object : Debuff() {
        override val name: String = Companion.name
        // Assume the caster is always maintaining this
        override val durationMs: Int = -1
        override val hidden: Boolean = true

        override fun modifyStats(sim: SimIteration): Stats? {
            return Stats(
                fireResistance = -88,
                frostResistance = -88
            )
        }
    }

    override fun cast(sim: SimIteration) {
        sim.addBuff(buff)
        sim.addDebuff(debuff)
    }
}
