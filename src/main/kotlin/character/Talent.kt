package character

abstract class Talent(val character: Character) {
    abstract var maxRank: Int
    abstract var currentRank: Int

    abstract fun apply()
}
