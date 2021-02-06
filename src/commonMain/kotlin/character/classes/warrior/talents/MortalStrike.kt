package character.classes.warrior.talents

import character.Talent

class MortalStrike(currentRank: Int) : Talent(currentRank) {
    companion object {
        const val name = "Mortal Strike"
    }

    override val name: String = Companion.name
    override val maxRank: Int = 1
}
