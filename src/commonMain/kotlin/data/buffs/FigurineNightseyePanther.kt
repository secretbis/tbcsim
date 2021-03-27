package data.buffs

import character.Ability
import character.Buff
import character.Stats
import sim.SimParticipant

class FigurineNightseyePanther : Buff() {
    companion object {
        const val name = "Figurine - Nightseye Panther (static)"
    }
    override val id: Int = 31047
    override val name: String = Companion.name
    override val durationMs: Int = -1
    override val hidden: Boolean = true

    val buffDurationMs = 12000
    val buff = object : Buff() {
        override val name: String  = "Figurine - Nightseye Panther"
        override val durationMs: Int = buffDurationMs

        override fun modifyStats(sp: SimParticipant): Stats? {
            return Stats(
                attackPower = 320,
                rangedAttackPower = 320
            )
        }
    }

    val ability = object : Ability() {
        override val id: Int = 31047
        override val name: String = "Figurine - Nightseye Panther"
        override fun gcdMs(sp: SimParticipant): Int = 0
        override fun cooldownMs(sp: SimParticipant): Int = 180000

        override fun trinketLockoutMs(sp: SimParticipant): Int = buffDurationMs

        override fun cast(sp: SimParticipant) {
            sp.addBuff(buff)
        }
    }

    override fun activeTrinketAbility(sp: SimParticipant): Ability = ability
}
