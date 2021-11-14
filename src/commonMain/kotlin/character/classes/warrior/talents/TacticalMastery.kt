package character.classes.warrior.talents

import character.Talent

class TacticalMastery(currentRank: Int) : Talent(currentRank) {
    fun rageRetained(): Int {
        return currentRank * 5
    }

    companion object {
        const val name = "Tactical Mastery"
    }

    override val name: String = Companion.name
    override val maxRank: Int = 3

    // TODO: Stance swapping isn't really a thing for DPS in TBC
}
