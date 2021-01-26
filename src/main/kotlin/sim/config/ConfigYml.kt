package sim.config

import kotlinx.serialization.Serializable

@Serializable
data class GearYml(
    val mainHand: GearItemYml? = null,
    val offHand: GearItemYml? = null,
    val rangedLibramTotem: GearItemYml? = null,
    val helm: GearItemYml? = null,
    val neck: GearItemYml? = null,
    val shoulders: GearItemYml? = null,
    val back: GearItemYml? = null,
    val chest: GearItemYml? = null,
    val wrists: GearItemYml? = null,
    val hands: GearItemYml? = null,
    val waist: GearItemYml? = null,
    val legs: GearItemYml? = null,
    val feet: GearItemYml? = null,
    val ring1: GearItemYml? = null,
    val ring2: GearItemYml? = null,
    val trinket1: GearItemYml? = null,
    val trinket2: GearItemYml? = null,
)

@Serializable
data class GearItemYml(
    val name: String,
    val enchant: String? = null,
    val gems: List<String>? = null
)

@Serializable
data class GearCompareYml(
    val name: String? = null
)

@Serializable
data class TalentYml(
    val name: String,
    val rank: Int
)

@Serializable
data class RotationYml(
    val precombat: List<RotationRuleYml>? = null,
    val combat: List<RotationRuleYml>
)

@Serializable
data class RotationRuleYml(
    val name: String,
    val condition: ConditionYml? = null
)

@Serializable
data class ConditionYml(
    val rule: String
)

@Serializable
data class SimOptionsYml(
    val durationMs: Int? = null,
    val stepMs: Int? = null,
    val latencyMs: Int? = null,
    val iterations: Int? = null,
    val targetLevel: Int? = null,
    val targetArmor: Int? = null,
    val allowParryAndBlock: Boolean? = null
)

@Serializable
data class ConfigYml(
    val `class`: String,
    val race: String,
    val level: Int = 70,
    val talents: List<TalentYml>?,
    val gear: GearYml? = null,
    val gearCompare: GearCompareYml? = null,
    val rotation: RotationYml? = null,
    val simOpts: SimOptionsYml? = null
)
