package character.classes.mage.talents

import character.Talent

class IceShards(currentRank: Int) : Talent(currentRank) {
    companion object {
        const val name = "Ice Shards"
    }
    override val name: String = Companion.name
    override val maxRank: Int = 5

    fun frostCritDamageBonusPct(): Double = 20.0 * currentRank
}
