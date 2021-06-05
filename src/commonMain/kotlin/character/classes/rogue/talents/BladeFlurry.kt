package character.classes.rogue.talents

import character.*

class BladeFlurry(currentRank: Int) : Talent(currentRank) {
    companion object {
        const val name = "Blade Flurry"
    }

    override val name: String = Companion.name
    override val maxRank: Int = 1
}