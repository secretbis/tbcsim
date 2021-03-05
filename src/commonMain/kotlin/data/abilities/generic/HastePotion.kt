package data.abilities.generic

import character.*
import sim.SimIteration

class HastePotion : Ability() {
    companion object {
        const val name = "Haste Potion"
    }

    override val id: Int = 22838
    override val name: String = Companion.name
    override fun gcdMs(sim: SimIteration): Int = 0
    override val castableOnGcd = true
    override val sharedCooldown: SharedCooldown = SharedCooldown.POTION
    override fun cooldownMs(sim: SimIteration): Int = 120000

    val buff = object : Buff() {
        override val name: String = "Haste Potion"
        override val durationMs: Int = 15000
        override val mutex: List<Mutex> = listOf(Mutex.POTION)

        override fun modifyStats(sim: SimIteration): Stats {
            return Stats(physicalHasteRating = 400.0)
        }
    }

    override fun cast(sim: SimIteration) {
        sim.addBuff(buff)
    }
}
