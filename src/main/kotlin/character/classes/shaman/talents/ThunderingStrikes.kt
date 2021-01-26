package character.classes.shaman.talents

import character.Talent

class ThunderingStrikes(ranks: Int) : Talent(ranks) {
    companion object {
        const val name = "Thundering Strikes"
    }

    override val name: String = Companion.name
    override val maxRank: Int = 1
}
