package character.classes.hunter.talents

import character.Talent

class AimedShot(currentRank: Int) : Talent(currentRank) {
    companion object {
        const val name = "Aimed Shot"
    }
    override val name: String = Companion.name
    override val maxRank: Int = 1
}
