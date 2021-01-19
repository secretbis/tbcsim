package character

import sim.Sim

abstract class Talent(val character: Character) {
    abstract var maxRank: Int
    abstract var currentRank: Int

    // If needed, inject additional procs or buffs into the sim state
    abstract fun apply(sim: Sim)
}
