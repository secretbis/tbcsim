package character.classes.warrior.talents

import character.Talent

class TacticalMastery(currentRank: Int) : Talent(currentRank) {
    companion object {
        const val name = "Tactical Mastery"
    }

    override val name: String = Companion.name
    override val maxRank: Int = 3

    // TODO: Stance swapping isn't really a thing for DPS in TBC
}
