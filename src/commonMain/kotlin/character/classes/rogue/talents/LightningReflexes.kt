package character.classes.rogue.talents

import character.*

class LightningReflexes(currentRank: Int) : Talent(currentRank) {
    companion object {
        const val name = "Lightning Reflexes"
    }

    override val name: String = Companion.name
    override val maxRank: Int = 5
}