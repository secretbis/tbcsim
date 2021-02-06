package character.classes.warrior.talents

import character.Talent

class ImprovedHeroicStrike(currentRank: Int) : Talent(currentRank) {
    companion object {
        const val name = "Improved Heroic Strike"
    }

    override val name: String = Companion.name
    override val maxRank: Int = 3
}
