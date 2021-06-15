package character.classes.priest.pet

import character.*
import character.classes.priest.pet.abilities.Melee
import character.classes.priest.pet.buffs.ShadowfiendBase
import data.model.Item

class Shadowfiend : Class(mapOf(), ShadowfiendSpec()) {
    override var baseStats: Stats = Stats()

    override fun talentFromString(name: String, ranks: Int): Talent? {
        return null
    }

    override fun abilityFromString(name: String, item: Item?): Ability? {
        return when(name) {
            "Melee" -> Melee()
            else -> null
        }
    }

    override val buffs: List<Buff> = listOf(ShadowfiendBase())

    override val resourceTypes: List<Resource.Type> = listOf(Resource.Type.MANA)
    override var canDualWield: Boolean = false
    override var attackPowerFromAgility: Int = 0
    override var attackPowerFromStrength: Int = 0
    override val critPctPerAgility: Double = 0.0
    override val dodgePctPerAgility: Double = 0.0
    override val baseDodgePct: Double = 0.0
    override var rangedAttackPowerFromAgility: Int = 0
    override val baseMana: Int = 2241
    override val baseSpellCritChance: Double = 0.0
}
