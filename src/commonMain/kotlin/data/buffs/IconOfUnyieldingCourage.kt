package data.buffs

import character.Ability
import character.Buff
import character.Stats
import sim.SimParticipant

class IconOfUnyieldingCourage : Buff() {
    companion object {
        const val name = "Icon of Unyielding Courage"
    }
    override val id: Int = 34106
    override val name: String = Companion.name + " (static)"
    override val icon: String = "inv_brd_banner.jpg"
    override val durationMs: Int = -1
    override val hidden: Boolean = true

    val buffDurationMs = 20000
    val buff = object : Buff() {
        override val name: String = Companion.name
        override val icon: String = "inv_brd_banner.jpg"
        override val durationMs: Int = buffDurationMs

        override fun modifyStats(sp: SimParticipant): Stats? {
            return Stats(armorPen = 600)
        }
    }

    val ability = object : Ability() {
        override val id: Int = 34106
        override val name: String = Companion.name
        override val icon: String = "inv_brd_banner.jpg"
        override fun gcdMs(sp: SimParticipant): Int = 0
        override fun cooldownMs(sp: SimParticipant): Int = 120000

        override fun trinketLockoutMs(sp: SimParticipant): Int = buffDurationMs

        override fun cast(sp: SimParticipant) {
            sp.addBuff(buff)
        }
    }

    override fun activeTrinketAbility(sp: SimParticipant): Ability = ability
}
