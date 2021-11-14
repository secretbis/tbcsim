package character.classes.warrior.talents

import character.Talent

class ImprovedSunderArmor(currentRank: Int) : Talent(currentRank) {
    companion object {
        const val name = "Improved Sunder Armor"
    }

    override val name: String = Companion.name
    override val maxRank: Int = 3
}
