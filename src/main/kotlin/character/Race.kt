package character

import character.races.Draenei

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
    abstract var racials: List<Ability>
}
