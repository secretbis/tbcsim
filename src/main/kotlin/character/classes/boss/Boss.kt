package character.classes.boss

import character.*
import data.model.Item

class Boss : Class(mapOf()) {
    override fun talentFromString(name: String, ranks: Int): Talent? {
        return null
    }

    override fun abilityFromString(name: String, item: Item?): Ability? {
        return null
    }

    override var baseStats: Stats = Stats()
    override var buffs: List<Buff> = listOf()
    override var resourceType: Resource.Type = Resource.Type.MANA
    override var baseResourceAmount: Int = 0
    override var canDualWield: Boolean = false
    override var allowAutoAttack: Boolean = false
    override var attackPowerFromAgility: Int = 0
    override var attackPowerFromStrength: Int = 0
    override val critPctPerAgility: Double = 0.0
    override var rangedAttackPowerFromAgility: Int = 0
}
