package character.races

import character.Race
import character.Ability
import character.Stats
import sim.Sim

class Tauren : Race() {
    override var baseStats: Stats = Stats(
        strength = 5,
        agility = -4,
        stamina = 1,
        intellect = -4,
        spirit = 2
    )
    override var racials: List<Ability> = listOf()

}
