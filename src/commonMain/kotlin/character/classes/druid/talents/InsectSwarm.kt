package character.classes.druid.talents

import character.Talent

/**
 *
 */
class InsectSwarm(currentRank: Int) : Talent(currentRank) {
    companion object {
        const val name = "Insect Swarm"
    }

    override val name: String = Companion.name
    override val maxRank: Int = 1
}