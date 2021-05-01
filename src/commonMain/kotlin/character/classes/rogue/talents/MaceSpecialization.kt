package character.classes.rogue.talents

import character.*

class MaceSpecialization(currentRank: Int) : Talent(currentRank) {
    companion object {
        const val name = "Mace Specialization"
    }

    override val name: String = Companion.name
    override val maxRank: Int = 5

    // not implemented, I don't think any mainstream spec uses this anyways in PvE
}