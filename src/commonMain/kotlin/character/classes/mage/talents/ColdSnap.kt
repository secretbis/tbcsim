package character.classes.mage.talents

import character.Talent

class ColdSnap(currentRank: Int) : Talent(currentRank) {
    companion object {
        const val name = "Cold Snap"
    }
    override val name: String = Companion.name
    override val maxRank: Int = 1
}
