package character.classes.warrior.talents

import character.Talent

class EndlessRage(currentRank: Int) : Talent(currentRank) {
    companion object {
        const val name = "Endless Rage"
    }

    override val name: String = Companion.name
    override val maxRank: Int = 1
}
