package character.classes.shaman.talents

import character.Talent

class LightningOverload(currentRank: Int) : Talent(currentRank) {
    companion object {
        const val name = "Lightning Overload"
    }

    override val name: String = Companion.name
    override val maxRank: Int = 5
}
