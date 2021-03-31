package character.classes.hunter.talents

import character.Talent

class BestialDiscipline(currentRank: Int) : Talent(currentRank) {
    companion object {
        const val name = "Bestial Discipline"
    }
    override val name: String = Companion.name
    override val maxRank: Int = 2

    fun petFocusRegenMultiplier(): Double = 1.0 + (currentRank * 0.5)
}
