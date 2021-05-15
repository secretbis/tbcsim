package character.classes.mage.talents

import character.Talent

class ArcaneImpact(currentRank: Int) : Talent(currentRank) {
    companion object {
        const val name = "Arcane Impact"
    }
    override val name: String = Companion.name
    override val maxRank: Int = 3

    fun arcaneBlastExplosionAddlCritChance(): Double = 2.0 * currentRank
}
