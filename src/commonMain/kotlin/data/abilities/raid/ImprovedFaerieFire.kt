package data.abilities.raid

import character.*
import mechanics.Rating
import sim.SimParticipant

class ImprovedFaerieFire : Ability() {
    companion object {
        const val name = "Improved Faerie Fire"
    }

    override val id: Int = 26993
    override val name: String = Companion.name
    override val icon: String = "spell_nature_faeriefire.jpg"
    override fun gcdMs(sp: SimParticipant): Int = 0

    fun debuff(owner: SimParticipant) = object : Debuff(owner) {
        override val name: String = "Improved Faerie Fire"
        override val icon: String = "spell_nature_faeriefire.jpg"
        // Assume the caster is always maintaining this
        override val durationMs: Int = -1
        override val hidden: Boolean = false

        override val mutex: List<Mutex> = listOf(Mutex.BUFF_FAERIE_FIRE)
        override fun mutexPriority(sp: SimParticipant): Map<Mutex, Int> {
            return mapOf(
                Mutex.BUFF_FAERIE_FIRE to 3
            )
        }

        override fun modifyStats(sp: SimParticipant): Stats {
            return Stats(
                armor = -610
            )
        }
    }

    val buff = object : Buff() {
        override val name: String = "Improved Faerie Fire"
        override val icon: String = "spell_nature_faeriefire.jpg"
        override val durationMs: Int = -1
        override val hidden: Boolean = true

        override fun modifyStats(sp: SimParticipant): Stats {
            return Stats(physicalHitRating = 3.0 * Rating.physicalHitPerPct)
        }
    }

    override fun cast(sp: SimParticipant) {
        sp.addBuff(buff)
        sp.sim.target.addDebuff(debuff(sp))
    }
}
