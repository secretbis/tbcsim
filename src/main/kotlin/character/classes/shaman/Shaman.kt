package character.classes.shaman

import character.*
import character.classes.shaman.talents.UnleashedRage

class Shaman : Class {

    override var baseStats: Stats = Stats(
        agility = 222,
        intellect = 180,
        strength = 108,
        stamina = 154,
        spirit = 135
    )
    override var abilities: List<Ability> = listOf()
    override var buffs: List<Buff> = listOf()
    override var talents: List<Talent> = listOf(
        UnleashedRage()
    )
    override var procs: List<Proc> = listOf()

    override var resourceType: Resource.Type = Resource.Type.MANA
    override var baseResourceAmount: Int = 0

    override var canDualWield: Boolean = true
    override var allowAutoAttack: Boolean = true

    override var attackPowerFromAgility: Int = 0
    override var attackPowerFromStrength: Int = 2

    // Ability state

    // Windfury weapon has a shared cooldown of 3s across both hands
    var lastWindfuryWeaponProcMs: Int = -1
}
