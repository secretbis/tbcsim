package character.classes.shaman.talents

import character.Talent

class ShamanisticRage(ranks: Int) : Talent(ranks) {
    companion object {
        const val name = "Shamanistic Rage"
    }

    override val name: String = Companion.name
    override val maxRank: Int = 1
}
