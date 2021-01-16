package character

import sim.Sim

abstract class Race (val sim: Sim) : Character by sim.subject {
    abstract var baseStats: Stats
    abstract var racials: List<Ability>
}
