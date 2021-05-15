package character.classes.mage.talents

import character.Talent

class IceFloes(currentRank: Int) : Talent(currentRank) {
    companion object {
        const val name = "Ice Floes"
    }
    override val name: String = Companion.name
    override val maxRank: Int = 2

    fun frostCooldownReductionMultiplier(): Double = 1.0 - (currentRank * 0.1)
}
