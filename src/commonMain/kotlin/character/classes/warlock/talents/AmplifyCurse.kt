package character.classes.warlock.talents

import character.Talent

class AmplifyCurse(currentRank: Int) : Talent(currentRank) {
    companion object {
        const val name = "Amplify Curse"
    }
    override val name: String = "Amplify Curse"
    override val maxRank: Int = 1
}
