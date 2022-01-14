package data.buffs

import character.Ability
import character.Buff
import character.Stats
import sim.SimParticipant

class BloodlustBrooch : Buff() {
    companion object {
        const val name = "Bloodlust Brooch"
    }
    override val id: Int = 35166
    override val name: String = Companion.name + " (static)"
    override val icon: String = "inv_misc_monsterscales_15.jpg"
    override val durationMs: Int = -1
    override val hidden: Boolean = true

    val buffDurationMs = 20000
    val apBuff = object : Buff() {
        override val name: String = Companion.name
        override val icon: String = "inv_misc_monsterscales_15.jpg"
        override val durationMs: Int = buffDurationMs

        override fun modifyStats(sp: SimParticipant): Stats? {
            return Stats(
                attackPower = 278,
                rangedAttackPower = 278
            )
        }
    }

    val ability = object : Ability() {
        override val id: Int = 35166
        override val name: String = Companion.name
        override val icon: String = "inv_misc_monsterscales_15.jpg"
        override fun gcdMs(sp: SimParticipant): Int = 0
        override fun cooldownMs(sp: SimParticipant): Int = 120000

        override fun trinketLockoutMs(sp: SimParticipant): Int = buffDurationMs

        override fun cast(sp: SimParticipant) {
            sp.addBuff(apBuff)
        }
    }

    override fun activeTrinketAbility(sp: SimParticipant): Ability = ability
}
