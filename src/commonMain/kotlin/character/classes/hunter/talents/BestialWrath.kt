package character.classes.hunter.talents

import character.Talent

class BestialWrath(currentRank: Int) : Talent(currentRank) {
    companion object {
        const val name = "Bestial Wrath"
    }
    override val name: String = Companion.name
    override val maxRank: Int = 1
}
