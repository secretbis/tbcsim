package character.classes.rogue.talents

import character.*

class MaceSpecialization(currentRank: Int) : Talent(currentRank) {
    companion object {
        const val name = "Mace Specialization"
    }

    override val name: String = Companion.name
    override val maxRank: Int = 5

    // TODO: implement for Dragonmaw I guess, I don't think there are any other PvE maces
}