package data.abilities.raid

import character.Ability
import character.Buff
import character.Stats
import sim.SimParticipant

class ImprovedBlessingOfWisdom : Ability() {
    companion object {
        const val name = "Improved Blessing of Wisdom"
    }

    override val id: Int = 20217
    override val name: String = Companion.name
    override fun gcdMs(sp: SimParticipant): Int = 0

    val buff = object : Buff() {
        override val name: String = "Blessing of Wisdom"
        // Assume the caster is always maintaining this
        override val durationMs: Int = -1
        override val hidden: Boolean = true

        override fun modifyStats(sp: SimParticipant): Stats {
            // Assume caster has 2/2 imp wis
            return Stats(manaPer5Seconds = (41 * 1.2).toInt())
        }
    }

    override fun cast(sp: SimParticipant) {
        sp.addBuff(buff)
    }
}
