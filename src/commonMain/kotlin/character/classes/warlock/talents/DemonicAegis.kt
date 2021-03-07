package character.classes.warlock.talents

import character.Talent

class DemonicAegis(currentRank: Int) : Talent(currentRank) {
    companion object {
        const val name = "Demonic Aegis"
    }
    override val name: String = Companion.name
    override val maxRank: Int = 3

    fun improvedArmorMultiplier(): Double {
        return 1.0 + (0.1 * currentRank)
    }
}
