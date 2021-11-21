package data.abilities.generic

import character.*
import sim.SimParticipant

class HastePotion : Ability() {
    companion object {
        const val name = "Haste Potion"
    }

    override val id: Int = 22838
    override val name: String = Companion.name
    override val icon: String = "inv_potion_108.jpg"
    override fun gcdMs(sp: SimParticipant): Int = 0
    override val castableOnGcd = true
    override val sharedCooldown: SharedCooldown = SharedCooldown.POTION
    override fun cooldownMs(sp: SimParticipant): Int = 120000

    val buff = object : Buff() {
        override val name: String = "Haste Potion"
        override val icon: String = "inv_potion_108.jpg"
        override val durationMs: Int = 15000
        override val mutex: List<Mutex> = listOf(Mutex.POTION)

        override fun modifyStats(sp: SimParticipant): Stats {
            return Stats(physicalHasteRating = 400.0)
        }
    }

    override fun cast(sp: SimParticipant) {
        sp.addBuff(buff)
    }
}
