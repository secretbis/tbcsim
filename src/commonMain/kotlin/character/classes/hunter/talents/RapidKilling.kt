package character.classes.hunter.talents

import character.Talent

class RapidKilling(currentRank: Int) : Talent(currentRank) {
    companion object {
        const val name = "Rapid Killing"
    }
    override val name: String = Companion.name
    override val maxRank: Int = 2

    fun rapidFireCooldownReductionMs(): Int {
        return 60000 * currentRank
    }
}
