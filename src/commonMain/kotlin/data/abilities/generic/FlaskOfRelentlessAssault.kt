package data.abilities.generic

import character.*
import sim.SimParticipant

class FlaskOfRelentlessAssault : Ability() {
    companion object {
        const val name = "Flask of Relentless Assault"
    }

    override val id: Int = 22854
    override val name: String = Companion.name
    override fun gcdMs(sp: SimParticipant): Int = 0

    val buff = object : Buff() {
        override val name: String = "Flask of Relentless Assault"
        override val durationMs: Int = 2 * 60 * 60 * 1000
        override val mutex: List<Mutex> = listOf(Mutex.BATTLE_ELIXIR, Mutex.GUARDIAN_ELIXIR)

        override fun modifyStats(sp: SimParticipant): Stats {
            return Stats(
                attackPower = 120,
                rangedAttackPower = 120
            )
        }
    }

    override fun cast(sp: SimParticipant) {
        sp.addBuff(buff)
    }
}
