package character.classes.priest.talents

import character.Talent

class VampiricTouch(currentRank: Int) : Talent(currentRank) {
    companion object {
        const val name = "Vampiric Touch"
    }
    override val name: String = Companion.name
    override val maxRank: Int = 1
}
