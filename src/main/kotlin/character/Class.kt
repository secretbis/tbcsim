package character

import character.classes.shaman.Shaman
import data.model.Item

abstract class Class(
    var talents: Map<String, Talent>
) {
    companion object {
        fun fromString(name: String, talents: Map<String, Talent> = mapOf()): Class? {
            return when(name.toLowerCase().trim()) {
                "shaman" -> Shaman(talents)
                else -> null
            }
        }
    }

    // Everything that comes with the class
    abstract val baseStats: Stats
    abstract val buffs: List<Buff>

    // Ability factory
    abstract fun abilityFromString(name: String, item: Item? = null): Ability?

    // Talent factory
    abstract fun talentFromString(name: String, ranks: Int): Talent?

    // Class resource
    abstract val resourceType: Resource.Type
    abstract val baseResourceAmount: Int  // This refers to any base amount inherent to the *class*

    abstract val canDualWield: Boolean
    abstract val allowAutoAttack: Boolean

    abstract val attackPowerFromAgility: Int
    abstract val attackPowerFromStrength: Int
    abstract val critPctPerAgility: Double
    abstract val rangedAttackPowerFromAgility: Int
}
