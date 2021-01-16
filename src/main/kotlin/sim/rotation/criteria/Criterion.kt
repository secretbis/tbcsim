package sim.rotation.criteria

import sim.Sim

interface Criterion {
    fun satisfied(sim: Sim): Boolean

}