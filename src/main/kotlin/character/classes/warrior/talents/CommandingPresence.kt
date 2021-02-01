package character.classes.warrior.talents

import character.Talent

class CommandingPresence(currentRank: Int) : Talent(currentRank) {
    companion object {
        const val name = "Commanding Presence"
    }

    override val name: String = Companion.name
    override val maxRank: Int = 5

    fun shoutMultiplier(): Double = currentRank * 0.05
}
