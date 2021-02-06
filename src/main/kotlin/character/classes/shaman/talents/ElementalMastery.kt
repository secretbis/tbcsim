package character.classes.shaman.talents

import character.Talent

class ElementalMastery(currentRank: Int) : Talent(currentRank) {
    companion object {
        const val name = "Elemental Mastery"
    }
    override val name: String = Companion.name
    override val maxRank: Int = 1
}
