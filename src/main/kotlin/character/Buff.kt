package character

import sim.Sim

abstract class Buff {
    enum class ModType {
        FLAT,
        PERCENTAGE,
        NONE
    }

    abstract var appliedAtMs: Int
    abstract val durationMs: Int
    abstract val statModType: ModType

    open val hidden: Boolean = false
    open val stacksWithSelf: Boolean = false

    abstract fun modifyStats(sim: Sim, stats: Stats): Stats
}
