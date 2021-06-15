package character.classes.priest.talents

import character.Talent

class PowerInfusion(currentRank: Int) : Talent(currentRank) {
    companion object {
        const val name = "Power Infusion"
    }
    override val name: String = Companion.name
    override val maxRank: Int = 1
}
