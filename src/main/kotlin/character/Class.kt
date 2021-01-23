package character

abstract class Class(
    val talents: Map<String, Talent>
) {
    // Everything that comes with the class
    abstract val baseStats: Stats
    abstract val abilities: List<Ability>
    abstract val buffs: List<Buff>
    abstract val procs: List<Proc>

    // Class resource
    abstract val resourceType: Resource.Type
    abstract val baseResourceAmount: Int  // This refers to any base amount inherent to the *class*

    abstract val canDualWield: Boolean
    abstract val allowAutoAttack: Boolean

    abstract val attackPowerFromAgility: Int
    abstract val attackPowerFromStrength: Int
    abstract val rangedAttackPowerFromAgility: Int
}
