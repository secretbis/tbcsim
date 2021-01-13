package entity

abstract class Spell(val entity: Entity) {
    abstract fun cast()
}