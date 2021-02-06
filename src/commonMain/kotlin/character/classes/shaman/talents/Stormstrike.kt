package character.classes.shaman.talents

import character.Talent

class Stormstrike(ranks: Int) : Talent(ranks) {
    companion object {
        const val name = "Stormstrike"
    }

    override val name: String = Companion.name
    override val maxRank: Int = 1
}
