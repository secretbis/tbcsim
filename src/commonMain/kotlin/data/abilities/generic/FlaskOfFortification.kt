package data.abilities.generic

import character.*
import sim.SimParticipant

class FlaskOfFortification : Ability() {
    companion object {
        const val name = "Flask of Fortification"
    }

    override val id: Int = 22851
    override val name: String = Companion.name
    override val icon: String = "inv_potion_119.jpg"
    override fun gcdMs(sp: SimParticipant): Int = 0

    val buff = object : Buff() {
        override val name: String = Companion.name
        override val icon: String = "inv_potion_119.jpg"
        override val durationMs: Int = 2 * 60 * 60 * 1000
        override val mutex: List<Mutex> = listOf(Mutex.BATTLE_ELIXIR, Mutex.GUARDIAN_ELIXIR)

        override fun modifyStats(sp: SimParticipant): Stats {
            return Stats(
                healthFlatModifier = 500,
                defenseRating = 10.0
            )
        }
    }

    override fun cast(sp: SimParticipant) {
        sp.addBuff(buff)
    }
}
