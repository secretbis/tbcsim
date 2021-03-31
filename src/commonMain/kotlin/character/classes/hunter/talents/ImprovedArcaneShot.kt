package character.classes.hunter.talents

import character.Talent

class ImprovedArcaneShot(currentRank: Int) : Talent(currentRank) {
    companion object {
        const val name = "Improved Arcane Shot"
    }
    override val name: String = Companion.name
    override val maxRank: Int = 5

    fun arcaneShotCooldownReductionMs(): Int {
        return 200 * currentRank
    }
}
