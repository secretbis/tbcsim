package character.classes.warrior.talents

import character.Talent

class ImprovedWhirlwind(currentRank: Int) : Talent(currentRank) {
    companion object {
        const val name = "Improved Whirlwind"
    }

    override val name: String = Companion.name
    override val maxRank: Int = 2
}
