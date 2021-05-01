package character.classes.rogue.talents

import character.*

class ImprovedSinisterStrike(currentRank: Int) : Talent(currentRank) {
    companion object {
        const val name = "Improved Sinister Strike"
    }

    override val name: String = Companion.name
    override val maxRank: Int = 2

    fun reducedEnergy(): Double {
        return when(currentRank){
            1 -> 3.0,
            2 -> 5.0,
            else -> 0.0
        }
    }
}