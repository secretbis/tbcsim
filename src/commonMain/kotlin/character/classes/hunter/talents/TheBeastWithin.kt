package character.classes.hunter.talents

import character.Talent

class TheBeastWithin(currentRank: Int) : Talent(currentRank) {
    companion object {
        const val name = "The Beast Within"
    }
    override val name: String = Companion.name
    override val maxRank: Int = 1
}
