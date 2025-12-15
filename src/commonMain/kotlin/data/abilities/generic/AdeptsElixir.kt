package data.abilities.generic

import character.*
import sim.SimParticipant

class AdeptsElixir : Ability() {
    companion object {
        const val name = "Adept's Elixir"
    }

    override val id: Int = 28103
    override val name: String = Companion.name
    override val icon: String = "inv_potion_96.jpg"
    override fun gcdMs(sp: SimParticipant): Int = 0

    val buff = object : Buff() {
        override val name: String = "Adept's Elixir"
        override val icon: String = "inv_potion_96.jpg"
        override val durationMs: Int = 60 * 60 * 1000
        override val mutex: List<Mutex> = listOf(Mutex.GUARDIAN_ELIXIR)

        override fun modifyStats(sp: SimParticipant): Stats {
            return Stats(spellDamage = 24, spellCritRating = 24.0)
        }
    }

    override fun cast(sp: SimParticipant) {
        sp.addBuff(buff)
    }
}