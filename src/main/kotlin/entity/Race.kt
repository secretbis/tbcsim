package entity

abstract class Race (val entity: Entity) : Entity by entity {
    abstract var baseStats: Stats
    abstract var spells: List<Spell>
}
