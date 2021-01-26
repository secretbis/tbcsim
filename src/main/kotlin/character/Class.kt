package character

import character.classes.shaman.Shaman

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
    abstract val abilities: List<Ability>
    abstract val buffs: List<Buff>
    abstract val procs: List<Proc>

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
