package character

abstract class Buff {
    enum class ModType {
        FLAT,
        PERCENTAGE,
        NONE
    }

    abstract val ability: Ability
    abstract val durationMs: Int
    abstract val statModType: ModType
    abstract val hidden: Boolean

    abstract fun modifyStats(stats: Stats): Stats
}
