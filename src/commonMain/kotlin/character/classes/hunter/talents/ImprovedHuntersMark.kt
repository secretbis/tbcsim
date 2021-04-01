package character.classes.hunter.talents

import character.Talent

class ImprovedHuntersMark(currentRank: Int) : Talent(currentRank) {
    companion object {
        const val name = "Improved Hunter's Mark"
    }
    override val name: String = Companion.name
    override val maxRank: Int = 5
}
