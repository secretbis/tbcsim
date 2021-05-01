package character.classes.rogue.talents

import character.Talent

class ImprovedExposeArmor(currentRank: Int) : Talent(currentRank) {
    companion object {
        const val name = "Improved Expose Armor"
    }

    override val name: String = Companion.name
    override val maxRank: Int = 2

    fun exposeArmorMultiplier(): Double {
        return 1.0 + (currentRank * 0.25)
    }
}