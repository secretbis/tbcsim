package character.classes.mage.talents

import character.Talent

class BlastWave(currentRank: Int) : Talent(currentRank) {
    companion object {
        const val name = "Blast Wave"
    }
    override val name: String = Companion.name
    override val maxRank: Int = 1
}
