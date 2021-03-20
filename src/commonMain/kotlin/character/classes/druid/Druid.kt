package character.classes.druid

import character.*
import character.Class
import data.model.Item

class Druid : Class(mapOf()) {
    override fun talentFromString(name: String, ranks: Int): Talent? {
        return null
    }

    override fun abilityFromString(name: String, item: Item?): Ability? {
        return null
    }

    override var baseStats: Stats = Stats(
        agility = 222,
        intellect = 180,
        strength = 108,
        stamina = 154,
        spirit = 135
    )
    override var buffs: List<Buff> = listOf()

    override var resourceType: Resource.Type = Resource.Type.MANA
    override var canDualWield: Boolean = false
    override var attackPowerFromAgility: Int = 0
    override var attackPowerFromStrength: Int = 0
    override val critPctPerAgility: Double = 0.0
    override val dodgePctPerAgility: Double = 1.0 / 14.7
    override val baseDodgePct: Double = -1.87
    override var rangedAttackPowerFromAgility: Int = 0

    override val baseMana: Int = 2370
    override val baseSpellCritChance: Double = 1.85
}
