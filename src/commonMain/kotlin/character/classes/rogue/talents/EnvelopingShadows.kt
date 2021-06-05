package character.classes.rogue.talents

import character.*

class EnvelopingShadows(currentRank: Int) : Talent(currentRank) {
    companion object {
        const val name = "Enveloping Shadows"
    }

    override val name: String = Companion.name
    override val maxRank: Int = 3
}