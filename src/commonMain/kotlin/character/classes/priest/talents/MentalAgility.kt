package character.classes.priest.talents

import character.Talent

class MentalAgility(currentRank: Int) : Talent(currentRank) {
    companion object {
        const val name = "Mental Agility"
    }
    override val name: String = Companion.name
    override val maxRank: Int = 5

    fun instantSpellManaCostReductionMultiplier(): Double = 1.0 - (currentRank * 0.02)
}
