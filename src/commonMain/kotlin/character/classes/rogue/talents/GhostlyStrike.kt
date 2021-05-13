package character.classes.rogue.talents

import character.*

class GhostlyStrike(currentRank: Int) : Talent(currentRank) {
    companion object {
        const val name = "Ghostly Strike"
    }

    override val name: String = Companion.name
    override val maxRank: Int = 1
}