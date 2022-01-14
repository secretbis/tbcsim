package data.buffs

import character.Ability
import character.Buff
import character.Stats
import sim.SimParticipant

class KissOfTheSpider : Buff() {

    override val id: Int = 28866
    override val name: String = "Kiss of the Spider (static)"
    override val icon: String = "inv_trinket_naxxramas04.jpg"
    override val durationMs: Int = -1
    override val hidden: Boolean = true

    val buffDurationMs = 15000
    val buff = object : Buff() {
        override val name: String = "Kiss of the Spider"
        override val icon: String = "inv_trinket_naxxramas04.jpg"
        override val durationMs: Int = buffDurationMs

        override fun modifyStats(sp: SimParticipant): Stats? {
            return Stats(
                physicalHasteRating = 200.0
            )
        }
    }

    val ability = object : Ability() {
        override val id: Int = 28866
        override val name: String = "Kiss of the Spider"
        override val icon: String = "inv_trinket_naxxramas04.jpg"
        override fun gcdMs(sp: SimParticipant): Int = 0
        override fun cooldownMs(sp: SimParticipant): Int = 120000

        override fun trinketLockoutMs(sp: SimParticipant): Int = buffDurationMs

        override fun cast(sp: SimParticipant) {
            sp.addBuff(buff)
        }
    }

    override fun activeTrinketAbility(sp: SimParticipant): Ability = ability
}
