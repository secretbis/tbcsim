package entity

abstract class Talent(val entity: Entity) {
    abstract var maxRank: Int
    abstract var currentRank: Int

    abstract fun apply()
}
