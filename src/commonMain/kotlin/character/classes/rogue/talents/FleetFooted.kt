package character.classes.rogue.talents

import character.Talent

class FleetFooted(currentRank: Int) : Talent(currentRank) {
    companion object {
        const val name = "Fleet Footed"
    }

    override val name: String = Companion.name
    override val maxRank: Int = 2
}