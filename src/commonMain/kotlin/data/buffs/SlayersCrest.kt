package data.buffs

import character.Ability
import character.Buff
import character.Stats
import sim.SimParticipant

class SlayersCrest : Buff() {

    override val id: Int = 28777
    override val name: String = "Slayer's Crest (static)"
    override val icon: String = "inv_trinket_naxxramas03.jpg"
    override val durationMs: Int = -1
    override val hidden: Boolean = true

    val buffDurationMs = 20000
    val buff = object : Buff() {
        override val name: String  = "Slayer's Crest"
        override val icon: String = "inv_trinket_naxxramas03.jpg"
        override val durationMs: Int = buffDurationMs

        override fun modifyStats(sp: SimParticipant): Stats? {
            return Stats(
                rangedAttackPower = 260,
                attackPower = 260
            )
        }
    }

    val ability = object : Ability() {
        override val id: Int = 28866
        override val name: String = "Slayer's Crest"
        override val icon: String = "inv_trinket_naxxramas03.jpg"
        override fun gcdMs(sp: SimParticipant): Int = 0
        override fun cooldownMs(sp: SimParticipant): Int = 120000

        override fun trinketLockoutMs(sp: SimParticipant): Int = buffDurationMs

        override fun cast(sp: SimParticipant) {
            sp.addBuff(buff)
        }
    }

    override fun activeTrinketAbility(sp: SimParticipant): Ability = ability
}
