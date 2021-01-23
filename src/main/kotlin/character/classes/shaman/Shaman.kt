package character.classes.shaman

import character.*
import character.classes.shaman.talents.*

class Shaman(talents: Map<String, Talent>) : Class(talents) {

    override var baseStats: Stats = Stats(
        agility = 222,
        intellect = 180,
        strength = 108,
        stamina = 154,
        spirit = 135
    )

    override val abilities: List<Ability> = listOf()
    override val buffs: List<Buff> = listOf()
    override val procs: List<Proc> = listOf()

    override val resourceType: Resource.Type = Resource.Type.MANA
    override val baseResourceAmount: Int = 0

    override val canDualWield: Boolean
        get() = talents["Dual Wield"]?.currentRank == 1

    override val allowAutoAttack: Boolean = true

    override val attackPowerFromAgility: Int = 0
    override val attackPowerFromStrength: Int = 2
    override val rangedAttackPowerFromAgility: Int = 1
}
