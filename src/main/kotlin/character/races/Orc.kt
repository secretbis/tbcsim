package character.races

import character.Race
import character.Ability
import character.Stats
import sim.Sim

class Orc(sim: Sim) : Race(sim) {
    override var baseStats: Stats = Stats(
        strength = 3,
        agility = -3,
        stamina = 1,
        intellect = -3,
        spirit = 2
    )
    override var racials: List<Ability> = listOf()
}
