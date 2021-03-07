package character.classes.warlock.talents

import character.Talent

class ImprovedHealthstone(currentRank: Int) : Talent(currentRank) {
    companion object {
        const val name = "Improved Healthstone"
    }
    override val name: String = Companion.name
    override val maxRank: Int = 2
}
