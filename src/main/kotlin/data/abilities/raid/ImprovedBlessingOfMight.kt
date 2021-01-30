package data.abilities.raid

import character.Ability
import character.Buff
import character.Proc
import character.Stats
import sim.SimIteration

class ImprovedBlessingOfMight : Ability() {
    companion object {
        const val name = "Improved Blessing of Might"
    }

    override val id: Int = 27140
    override val name: String = Companion.name
    override fun gcdMs(sim: SimIteration): Int = 0

    // Always assume the raid buffer has 5/5 imp might
    val bonusAp = 220.0 * 1.2
    val buff = object : Buff() {
        override val name: String = Companion.name
        // Assume the caster is always maintaining this
        override val durationMs: Int = -1
        override val hidden: Boolean = true

        override fun modifyStats(sim: SimIteration): Stats {
            return Stats(attackPower = bonusAp.toInt())
        }
    }

    override fun cast(sim: SimIteration, free: Boolean) {
        sim.addBuff(buff)
    }

    override val baseCastTimeMs: Int = 0
}
