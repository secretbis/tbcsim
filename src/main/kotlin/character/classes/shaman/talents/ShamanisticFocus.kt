package character.classes.shaman.talents

import character.Talent

class ShamanisticFocus(ranks: Int) : Talent(ranks) {
    companion object {
        const val name = "Shamanistic Focus"
    }

    override val name: String = Companion.name
    override val maxRank: Int = 1
}
