package character.classes.mage.talents

import character.Talent

class PresenceOfMind(currentRank: Int) : Talent(currentRank) {
    companion object {
        const val name = "Presence of Mind"
    }
    override val name: String = Companion.name
    override val maxRank: Int = 1
}
