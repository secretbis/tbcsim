package character.classes.priest.talents

import character.Talent

class SearingLight(currentRank: Int) : Talent(currentRank) {
    companion object {
        const val name = "Searing Light"
    }
    override val name: String = Companion.name
    override val maxRank: Int = 2

    fun smiteHolyFireDamageMultiplier(): Double = 1.0 + (currentRank * 0.05)
}
