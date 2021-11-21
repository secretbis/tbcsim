package data.buffs

import character.Ability
import character.Buff
import character.Stats
import sim.SimParticipant

class AbacusOfViolentOdds : Buff() {
    companion object {
        const val name = "Abacus of Violent Odds (static)"
    }
    override val id: Int = 33807
    override val name: String = Companion.name
    override val icon: String = "inv_misc_enggizmos_18.jpg"
    override val durationMs: Int = -1
    override val hidden: Boolean = true

    val buffDurationMs = 10000
    val buff = object : Buff() {
        override val name: String  = "Abacus of Violent Odds"
        override val icon: String = "inv_misc_enggizmos_18.jpg"
        override val durationMs: Int = buffDurationMs

        override fun modifyStats(sp: SimParticipant): Stats? {
            return Stats(physicalHasteRating = 260.00)
        }
    }

    val ability = object : Ability() {
        override val id: Int = 33807
        override val name: String = "Abacus of Violent Odds"
        override val icon: String = "inv_misc_enggizmos_18.jpg"
        override fun gcdMs(sp: SimParticipant): Int = 0
        override fun cooldownMs(sp: SimParticipant): Int = 120000

        override fun trinketLockoutMs(sp: SimParticipant): Int = buffDurationMs

        override fun cast(sp: SimParticipant) {
            sp.addBuff(buff)
        }
    }

    override fun activeTrinketAbility(sp: SimParticipant): Ability = ability
}
