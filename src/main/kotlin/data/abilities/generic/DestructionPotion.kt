package data.abilities.generic

import character.Ability
import character.Buff
import character.Stats
import mechanics.Rating
import sim.SimIteration

class DestructionPotion : Ability() {
    companion object {
        const val name = "Destruction Potion"
    }

    override val id: Int = 22839
    override val name: String = Companion.name
    override fun gcdMs(sim: SimIteration): Int = 0
    override val castableOnGcd = true
    override val sharedCooldown: SharedCooldown = SharedCooldown.POTION
    override fun cooldownMs(sim: SimIteration): Int = 120000

    val buff = object : Buff() {
        override val name: String = Companion.name
        override val durationMs: Int = 15000
        override val mutex: List<Mutex> = listOf(Mutex.POTION)

        override fun modifyStats(sim: SimIteration): Stats {
            return Stats(
                spellDamage = 120,
                spellCritRating = 2.0 * Rating.critPerPct
            )
        }
    }

    override fun cast(sim: SimIteration) {
        sim.addBuff(buff)
    }
}
