package character.classes.boss

import character.*

class Boss : Class {
    override var baseStats: Stats = Stats()
    override var abilities: List<Ability> = listOf()
    override var buffs: List<Buff> = listOf()
    override var talents: List<Talent> = listOf()
    override var procs: List<Proc> = listOf()

    override var resourceType: Resource.Type = Resource.Type.MANA
    override var baseResourceAmount: Int = 0
    override var canDualWield: Boolean = false
    override var allowAutoAttack: Boolean = false
    override var attackPowerFromAgility: Int = 0
    override var attackPowerFromStrength: Int = 0
    override var rangedAttackPowerFromAgility: Int = 0
}
