package character.classes.boss

import character.*
import character.classes.boss.buffs.BossBase
import character.classes.boss.specs.BossSpec
import data.model.Item

class Boss(override var baseStats: Stats, buffs: List<Buff> = listOf()) : Class(mapOf(), BossSpec()) {
    override fun talentFromString(name: String, ranks: Int): Talent? {
        return null
    }

    override fun abilityFromString(name: String, item: Item?): Ability? {
        return null
    }

    override var buffs: List<Buff> = listOf(BossBase()) + buffs
    override val resourceTypes: List<Resource.Type> = listOf(Resource.Type.MANA)
    override var canDualWield: Boolean = false
    override var attackPowerFromAgility: Int = 0
    override var attackPowerFromStrength: Int = 0
    override val critPctPerAgility: Double = 0.0
    override val dodgePctPerAgility: Double = 0.0
    override val baseDodgePct: Double = 0.0
    override var rangedAttackPowerFromAgility: Int = 0

    override val baseMana: Int = 0
    override val baseSpellCritChance: Double = 0.0
}
