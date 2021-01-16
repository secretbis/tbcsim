package character.races

import character.Race
import character.Ability
import character.Stats
import sim.Sim

class Troll : Race() {
    override var baseStats: Stats = Stats(
        strength = 1,
        agility = 2,
        intellect = -4,
        spirit = 1
    )
    override var racials: List<Ability> = listOf()
}
