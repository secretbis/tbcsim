package character.classes.warrior.talents

import character.Talent

class ImprovedMortalStrike(currentRank: Int) : Talent(currentRank) {
    companion object {
        const val name = "Improved Mortal Strike"
    }

    override val name: String = Companion.name
    override val maxRank: Int = 5
}
