package character.classes.warrior.talents

import character.Talent

class DeathWish(currentRank: Int) : Talent(currentRank) {
    companion object {
        const val name = "Death Wish"
    }

    override val name: String = Companion.name
    override val maxRank: Int = 1
}
