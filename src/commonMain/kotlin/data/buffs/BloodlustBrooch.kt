package data.buffs

import character.Ability
import character.Buff
import character.Stats
import sim.SimIteration

class BloodlustBrooch : Buff() {
    companion object {
        const val name = "Bloodlust Brooch (static)"
    }
    override val id: Int = 35166
    override val name: String = Companion.name
    override val durationMs: Int = -1
    override val hidden: Boolean = true

    val buffDurationMs = 20000
    val apBuff = object : Buff() {
        override val name: String  = "Bloodlust Brooch"
        override val durationMs: Int = buffDurationMs

        override fun modifyStats(sim: SimIteration): Stats? {
            return Stats(attackPower = 278)
        }
    }

    val ability = object : Ability() {
        override val id: Int = 35166
        override val name: String = "Bloodlust Brooch"
        override fun gcdMs(sim: SimIteration): Int = 0
        override fun cooldownMs(sim: SimIteration): Int = 120000

        override fun trinketLockoutMs(sim: SimIteration): Int = buffDurationMs

        override fun cast(sim: SimIteration) {
            sim.addBuff(apBuff)
        }
    }

    override fun activeTrinketAbility(sim: SimIteration): Ability = ability
}
