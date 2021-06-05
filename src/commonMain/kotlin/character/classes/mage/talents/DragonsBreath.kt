package character.classes.mage.talents

import character.Talent

class DragonsBreath(currentRank: Int) : Talent(currentRank) {
    companion object {
        const val name = "Dragon's Breath"
    }
    override val name: String = Companion.name
    override val maxRank: Int = 1
}
