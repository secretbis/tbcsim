package sim.rotation.criteria

import sim.SimIteration

interface Criterion {
    fun satisfied(sim: SimIteration): Boolean

}
