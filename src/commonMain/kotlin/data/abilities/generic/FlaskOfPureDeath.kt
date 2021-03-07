package data.abilities.generic

import character.Ability
import character.Buff
import character.Mutex
import character.Stats
import sim.SimIteration

class FlaskOfPureDeath : Ability() {
    companion object {
        const val name = "Flask of Pure Death"
    }

    override val id: Int = 22866
    override val name: String = Companion.name
    override fun gcdMs(sim: SimIteration): Int = 0

    val buff = object : Buff() {
        override val name: String = Companion.name
        override val durationMs: Int = 2 * 60 * 60 * 1000
        override val mutex: List<Mutex> = listOf(Mutex.BATTLE_ELIXIR, Mutex.GUARDIAN_ELIXIR)

        override fun modifyStats(sim: SimIteration): Stats {
            return Stats(
                fireDamage = 80,
                frostDamage = 80,
                shadowDamage = 80,
            )
        }
    }

    override fun cast(sim: SimIteration) {
        sim.addBuff(buff)
    }
}
