package character.classes.hunter.talents

import character.Talent

class AnimalHandler(currentRank: Int) : Talent(currentRank) {
    companion object {
        const val name = "Animal Handler"
    }
    override val name: String = Companion.name
    override val maxRank: Int = 2

    fun petAdditionalHitChance(): Double = currentRank.toDouble()
}
