package data.abilities.raid

import character.*
import sim.SimParticipant

class ImprovedExposeArmor : Ability() {
    companion object {
        const val name = "Improved Expose Armor"
    }

    override val id: Int = 26866
    override val name: String = Companion.name
    override fun gcdMs(sp: SimParticipant): Int = 0

    val debuff = object : Debuff() {
        override val name: String = "Improved Expose Armor"
        // Assume the caster is always maintaining this
        override val durationMs: Int = -1
        override val hidden: Boolean = true

        override fun modifyStats(sp: SimParticipant): Stats {
            return Stats(
                armor = -1 * (2050 * 1.5).toInt()
            )
        }
    }

    override fun cast(sp: SimParticipant) {
        sp.addDebuff(debuff)
    }
}
