package character.classes.warlock.talents

import character.Talent

class DemonicSacrifice(currentRank: Int) : Talent(currentRank) {
    companion object {
        const val name = "Demonic Sacrifice"
    }
    override val name: String = Companion.name
    override val maxRank: Int = 1
}
