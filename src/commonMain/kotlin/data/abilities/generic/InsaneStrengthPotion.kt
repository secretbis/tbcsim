package data.abilities.generic

import character.Ability
import character.Buff
import character.Proc
import character.Stats
import sim.SimIteration

class InsaneStrengthPotion : Ability() {
    companion object {
        const val name = "Insane Strength Potion"
    }

    override val id: Int = 22828
    override val name: String = Companion.name
    override fun gcdMs(sim: SimIteration): Int = 0
    override val sharedCooldown: SharedCooldown = SharedCooldown.POTION
    override fun cooldownMs(sim: SimIteration): Int = 120000

    val buff = object : Buff() {
        override val name: String = Companion.name
        override val durationMs: Int = 15000
        override val mutex: List<Mutex> = listOf(Mutex.POTION)

        override fun modifyStats(sim: SimIteration): Stats {
            return Stats(strength = 120, defenseRating = -75.0)
        }
    }

    override fun cast(sim: SimIteration) {
        sim.addBuff(buff)
    }
}
