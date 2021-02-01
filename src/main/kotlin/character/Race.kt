package character

import character.races.Draenei
import sim.SimIteration

abstract class Race {
    companion object {
        fun fromString(name: String): Race? {
            return when(name.toLowerCase().trim()) {
                "draenei" -> return Draenei()
                else -> null
            }
        }
    }

    abstract var baseStats: Stats
    abstract fun racialByName(name: String): Ability?
    abstract fun buffs(sim: SimIteration): List<Buff>
}
