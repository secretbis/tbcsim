package character.classes.hunter.talents

import character.Talent

class Readiness(currentRank: Int) : Talent(currentRank) {
    companion object {
        const val name = "Readiness"
    }
    override val name: String = Companion.name
    override val maxRank: Int = 1
}
