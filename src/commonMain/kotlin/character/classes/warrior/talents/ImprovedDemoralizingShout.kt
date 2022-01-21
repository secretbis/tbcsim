package character.classes.warrior.talents

import character.Talent

class ImprovedDemoralizingShout(currentRank: Int) : Talent(currentRank) {
    companion object {
        const val name = "Improved Demoralizing Shout"
    }

    override val name: String = Companion.name
    override val maxRank: Int = 5
}
