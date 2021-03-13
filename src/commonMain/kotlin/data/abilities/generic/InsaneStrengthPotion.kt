package data.abilities.generic

import character.*
import sim.SimParticipant

class InsaneStrengthPotion : Ability() {
    companion object {
        const val name = "Insane Strength Potion"
    }

    override val id: Int = 22828
    override val name: String = Companion.name
    override fun gcdMs(sp: SimParticipant): Int = 0
    override val sharedCooldown: SharedCooldown = SharedCooldown.POTION
    override fun cooldownMs(sp: SimParticipant): Int = 120000

    val buff = object : Buff() {
        override val name: String = "Insane Strength Potion"
        override val durationMs: Int = 15000
        override val mutex: List<Mutex> = listOf(Mutex.POTION)

        override fun modifyStats(sp: SimParticipant): Stats {
            return Stats(strength = 120, defenseRating = -75.0)
        }
    }

    override fun cast(sp: SimParticipant) {
        sp.addBuff(buff)
    }
}
