package character.classes.shaman.talents

import character.Talent

class TotemOfWrath(currentRank: Int) : Talent(currentRank) {
    companion object {
        const val name = "Totem of Wrath"
    }
    override val name: String = Companion.name
    override val maxRank: Int = 1
}
