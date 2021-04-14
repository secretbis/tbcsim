package sim.config

import kotlinx.serialization.Serializable
import kotlin.js.JsExport

@JsExport
@Serializable
data class PartyYml(
    val buffs: List<String>? = null
)

@JsExport
@Serializable
data class GearYml(
    val mainHand: GearItemYml? = null,
    val offHand: GearItemYml? = null,
    val rangedTotemLibram: GearItemYml? = null,
    val ammo: GearItemYml? = null,
    val head: GearItemYml? = null,
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

@JsExport
@Serializable
data class GearItemYml(
    val name: String,
    val enchant: String? = null,
    val tempEnchant: String? = null,
    val gems: List<String>? = null
)

@JsExport
@Serializable
data class GearCompareYml(
    val name: String? = null
)

@JsExport
@Serializable
data class TalentYml(
    val name: String,
    val rank: Int
)

@JsExport
@Serializable
data class RotationYml(
    val autoAttack: Boolean? = null,
    val precombat: List<RotationRuleYml>? = null,
    val combat: List<RotationRuleYml>
)

@JsExport
@Serializable
data class RotationRuleOptions(
    val name: String? = null
)

@JsExport
@Serializable
data class RotationRuleYml(
    val name: String,
    val criteria: List<RotationRuleCriterion>? = null,
    val options: RotationRuleOptions? = null
)

@JsExport
@Serializable
data class RotationRuleCriterion(
    val type: String? = null,
    val buff: String? = null,
    val debuff: String? = null,
    val ability: String? = null,
    val seconds: Double? = null,
    val modulusSeconds: Int? = null,
    val pct: Int? = null,
    val oncePerSwing: Boolean? = null,
    val amount: Int? = null,
    val bool: Boolean? = null,
    val swingType: String? = null
)

@JsExport
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

@JsExport
@Serializable
data class PetYml(
    val type: String,
    val rotation: RotationYml? = null
)

@JsExport
@Serializable
data class ConfigYml(
    val `class`: String,
    val race: String,
    val spec: String,
    val description: String,
    val level: Int = 70,
    val epCategory: String? = null,
    val epSpec: String? = null,
    val talents: List<TalentYml>?,
    val gear: GearYml? = null,
    val gearCompare: GearCompareYml? = null,
    val rotation: RotationYml? = null,
    val raidBuffs: List<String>? = null,
    val raidDebuffs: List<String>? = null,
    val simOpts: SimOptionsYml? = null,
    val pet: PetYml? = null
)
