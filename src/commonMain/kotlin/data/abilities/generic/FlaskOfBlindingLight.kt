package data.abilities.generic

import character.Ability
import character.Buff
import character.Mutex
import character.Stats
import sim.SimParticipant

class FlaskOfBlindingLight : Ability() {
    companion object {
        const val name = "Flask of Blinding Light"
    }

    override val id: Int = 22861
    override val name: String = Companion.name
    override fun gcdMs(sp: SimParticipant): Int = 0

    val buff = object : Buff() {
        override val name: String = "Flask of Blinding Light"
        override val durationMs: Int = 2 * 60 * 60 * 1000
        override val mutex: List<Mutex> = listOf(Mutex.BATTLE_ELIXIR, Mutex.GUARDIAN_ELIXIR)

        override fun modifyStats(sp: SimParticipant): Stats {
            return Stats(
                arcaneDamage = 80,
                holyDamage = 80,
                natureDamage = 80,
            )
        }
    }

    override fun cast(sp: SimParticipant) {
        sp.addBuff(buff)
    }
}
