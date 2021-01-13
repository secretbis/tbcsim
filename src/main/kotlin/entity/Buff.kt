package entity

abstract class Buff(val entity: Entity) {
    abstract val spell: Spell
    abstract val durationMs: Int

    abstract fun apply()
    abstract fun remove()
}
