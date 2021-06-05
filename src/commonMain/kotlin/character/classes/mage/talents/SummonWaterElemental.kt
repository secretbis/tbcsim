package character.classes.mage.talents

import character.Talent

class SummonWaterElemental(currentRank: Int) : Talent(currentRank) {
    companion object {
        const val name = "Summon Water Elemental"
    }
    override val name: String = Companion.name
    override val maxRank: Int = 1
}
