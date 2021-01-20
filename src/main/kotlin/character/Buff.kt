package character

import sim.SimIteration

abstract class Buff {
    enum class ModType {
        FLAT,
        PERCENTAGE,
        PERCENTAGE_OF_PERCENTAGE,
        NONE
    }

    abstract var appliedAtMs: Int
    abstract val durationMs: Int
    abstract val statModType: ModType

    open val hidden: Boolean = false
    open val maxStacks: Int = 0
    open var currentStacks: Int = 0

    open fun reset() {
        currentStacks = 0
    }

    abstract fun modifyStats(sim: SimIteration, stats: Stats): Stats
    abstract val procs: List<Proc>
}
