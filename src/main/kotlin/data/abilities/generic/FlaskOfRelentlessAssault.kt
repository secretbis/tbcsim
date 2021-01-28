package data.abilities.generic

import character.Ability
import character.Buff
import character.Proc
import character.Stats
import sim.SimIteration

class FlaskOfRelentlessAssault : Ability() {
    companion object {
        const val name = "Flask of Relentless Assault"
    }

    override val id: Int = 22854
    override val name: String = Companion.name
    override fun gcdMs(sim: SimIteration): Int = 0

    val buff = object : Buff() {
        override val name: String = Companion.name
        override val durationMs: Int = 2 * 60 * 60 * 1000
        override val mutex: List<Mutex> = listOf(Mutex.BATTLE_ELIXIR, Mutex.GUARDIAN_ELIXIR)

        override fun modifyStats(sim: SimIteration, stats: Stats): Stats {
            return stats.add(Stats(attackPower = 120))
        }

        override fun procs(sim: SimIteration): List<Proc> = listOf()
    }

    override fun cast(sim: SimIteration, free: Boolean) {
        sim.addBuff(buff)
    }

    override val baseCastTimeMs: Int = 0
}
