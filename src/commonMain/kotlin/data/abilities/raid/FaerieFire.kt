package data.abilities.raid

import character.*
import sim.SimParticipant

// Since this is a deep Balance talent, it's not quite reasonable to assume 3/3 Improved
class FaerieFire : Ability() {
    companion object {
        const val name = "Faerie Fire"
    }

    override val id: Int = 26993
    override val name: String = Companion.name
    override fun gcdMs(sp: SimParticipant): Int = 0

    val debuff = object : Debuff() {
        override val name: String = "Faerie Fire"
        // Assume the caster is always maintaining this
        override val durationMs: Int = -1
        override val hidden: Boolean = true

        override fun modifyStats(sp: SimParticipant): Stats {
            return Stats(
                armor = -610
            )
        }
    }

    override fun cast(sp: SimParticipant) {
        sp.addDebuff(debuff)
    }
}
