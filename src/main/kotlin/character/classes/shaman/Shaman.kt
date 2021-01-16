package character.classes.shaman

import character.*

class Shaman : Class {

    override var baseStats: Stats = Stats(
        agility = 222,
        intellect = 180,
        strength = 108,
        stamina = 154,
        spirit = 135
    )
    override var abilities: List<Ability> = listOf()
    override var talents: List<Talent> = listOf()

    override var resourceType: Resource.Type = Resource.Type.MANA
    override var baseResourceAmount: Int = 0

    override var canDualWield: Boolean = true
    override var allowAutoAttack: Boolean = true
}
