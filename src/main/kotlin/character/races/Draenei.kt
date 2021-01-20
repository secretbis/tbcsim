package character.races

import character.Race
import character.Ability
import character.Stats
import sim.SimIteration

class Draenei: Race() {
    override var baseStats: Stats = Stats(
        strength = 1,
        agility = -3,
        spirit = 2
    )
    override var racials: List<Ability> = listOf()
}
