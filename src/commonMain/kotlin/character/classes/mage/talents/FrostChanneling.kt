package character.classes.mage.talents

import character.Talent

class FrostChanneling(currentRank: Int) : Talent(currentRank) {
    companion object {
        const val name = "Frost Channeling"
    }
    override val name: String = Companion.name
    override val maxRank: Int = 3

    fun frostSpellManaCostReductionMultiplier(): Double = 1.0 - (currentRank * 0.05)
}
