package character

abstract class Talent {
    abstract val name: String

    abstract val maxRank: Int
    abstract var currentRank: Int

    open val buffs: List<Buff> = listOf()
    open val procs: List<Proc> = listOf()
}
