package character

abstract class Buff {
    enum class ModType {
        FLAT,
        PERCENTAGE,
        NONE
    }

    abstract var appliedAtMs: Int
    abstract val durationMs: Int
    abstract val statModType: ModType
    abstract val hidden: Boolean

    abstract fun modifyStats(stats: Stats): Stats
}
