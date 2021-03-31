package character.classes.hunter.talents

import character.*

class FerociousInspiration(currentRank: Int) : Talent(currentRank) {
    companion object {
        const val name = "Ferocious Inspiration"
    }

    override val name: String = Companion.name
    override val maxRank: Int = 3
}
