package data.buffs

import character.Ability
import character.Buff
import character.Stats
import sim.SimParticipant

// This buff ID is used for multiple items
class ScryersBloodgemXirisGift(name: String) : Buff() {
    override val id: Int = 35337
    override val name: String = "$name (static)"
    override val durationMs: Int = -1
    override val hidden: Boolean = true

    val buffDurationMs = 15000
    val buff = object : Buff() {
        override val name: String  = name
        override val durationMs: Int = buffDurationMs

        override fun modifyStats(sp: SimParticipant): Stats {
            return Stats(spellDamage = 150)
        }
    }

    val ability = object : Ability() {
        override val id: Int = 35337
        override val name: String = name
        override fun gcdMs(sp: SimParticipant): Int = 0
        override fun cooldownMs(sp: SimParticipant): Int = 90000

        override fun trinketLockoutMs(sp: SimParticipant): Int = buffDurationMs

        override fun cast(sp: SimParticipant) {
            sp.addBuff(buff)
        }
    }

    override fun activeTrinketAbility(sp: SimParticipant): Ability = ability
}
