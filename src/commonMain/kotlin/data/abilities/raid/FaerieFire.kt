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
    override val icon: String = "spell_nature_faeriefire.jpg"
    override fun gcdMs(sp: SimParticipant): Int = 0

    fun debuff(owner: SimParticipant) = object : Debuff(owner) {
        override val name: String = "Faerie Fire"
        override val icon: String = "spell_nature_faeriefire.jpg"
        // Assume the caster is always maintaining this
        override val durationMs: Int = -1
        override val hidden: Boolean = false

        override val mutex: List<Mutex> = listOf(Mutex.BUFF_FAERIE_FIRE)
        override fun mutexPriority(sp: SimParticipant): Map<Mutex, Int> {
            return mapOf(
                Mutex.BUFF_FAERIE_FIRE to 0
            )
        }

        override fun modifyStats(sp: SimParticipant): Stats {
            return Stats(
                armor = -610
            )
        }
    }

    override fun cast(sp: SimParticipant) {
        sp.sim.target.addDebuff(debuff(sp))
    }
}
