package data.buffs

import character.Ability
import character.Buff
import character.Stats
import sim.SimParticipant

class BerserkersCall : Buff() {
    companion object {
        const val name = "Berserker's Call (static)"
    }

    override val name: String = Companion.name
    override val durationMs: Int = -1
    override val hidden: Boolean = true

    val buffDurationMs = 20000
    val apBuff = object : Buff() {
        override val name: String  = "Berserker's Call"
        override val durationMs: Int = buffDurationMs

        override fun modifyStats(sp: SimParticipant): Stats? {
            return Stats(
                attackPower = 360,
                rangedAttackPower = 360
            )
        }
    }

    val ability = object : Ability() {
        override val id: Int = 43716
        override val name: String = "Berserker's Call"
        override fun gcdMs(sp: SimParticipant): Int = 0
        override fun cooldownMs(sp: SimParticipant): Int = 120000

        override fun trinketLockoutMs(sp: SimParticipant): Int = buffDurationMs

        override fun cast(sp: SimParticipant) {
            sp.addBuff(apBuff)
        }
    }

    override fun activeTrinketAbility(sp: SimParticipant): Ability = ability
}
