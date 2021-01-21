package character

abstract class Talent(private var _currentRank: Int) {
    abstract val name: String
    abstract val maxRank: Int

    // Ensure we can't go above max rank
    val currentRank: Int
        get() {
            return if(_currentRank > maxRank) {
                maxRank
            } else _currentRank
        }

    open val buffs: List<Buff> = listOf()
    open val procs: List<Proc> = listOf()
}
