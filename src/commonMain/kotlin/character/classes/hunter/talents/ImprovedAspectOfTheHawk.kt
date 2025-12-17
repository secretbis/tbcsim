package character.classes.hunter.talents

import character.*

class ImprovedAspectOfTheHawk(currentRank: Int) : Talent(currentRank) {
    companion object {
        const val name = "Improved Aspect of the Hawk"
    }

    override val name: String = Companion.name
    override val maxRank: Int = 5
}
