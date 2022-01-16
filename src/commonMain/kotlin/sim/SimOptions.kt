package sim

import kotlin.js.JsExport
import kotlinx.serialization.Serializable

@JsExport
@Serializable
data class SimOptions(
    // Sim config
    var durationMs: Int = SimDefaults.durationMs,
    var durationVariabilityMs: Int = SimDefaults.durationVariabilityMs,
    var stepMs: Int = SimDefaults.stepMs,
    var latencyMs: Int = SimDefaults.latencyMs,
    var iterations: Int = SimDefaults.iterations,

    // Target config
    var targetProfile: String? = null,
    var targetLevel: Int = SimDefaults.targetLevel,
    var targetArmor: Int = SimDefaults.targetArmor,
    var targetType: Int = SimDefaults.targetType,
    var targetActive: Boolean = SimDefaults.targetActive,
    var targetAutoAttackSpeedMs: Int = SimDefaults.targetAutoAttackSpeedMs,
    var targetWeaponPower: Int = SimDefaults.targetWeaponPower,
    var targetDualWield: Boolean = SimDefaults.targetDualWield,

    // Other
    var allowParryAndBlock: Boolean = SimDefaults.allowParryAndBlock,
    var showHiddenBuffs: Boolean = SimDefaults.showHiddenBuffs,
    var randomSeed: Long? = null
) {
    fun merge(opts: SimOptions?): SimOptions {
        // Merge two SimOptions objects.  Prefer the argument's value if it is non-default.
        if(opts == null) return this

        return SimOptions(
            durationMs = if(opts.durationMs != SimDefaults.durationMs) opts.durationMs else durationMs,
            durationVariabilityMs = if(opts.durationVariabilityMs != SimDefaults.durationVariabilityMs) opts.durationVariabilityMs else durationVariabilityMs,
            stepMs = if(opts.stepMs != SimDefaults.stepMs) opts.stepMs else stepMs,
            latencyMs = if(opts.latencyMs != SimDefaults.latencyMs) opts.latencyMs else latencyMs,
            iterations = if(opts.iterations != SimDefaults.iterations) opts.iterations else iterations,

            targetProfile = if(opts.targetProfile != null) opts.targetProfile else targetProfile,
            targetLevel = if(opts.targetLevel != SimDefaults.targetLevel) opts.targetLevel else targetLevel,
            targetArmor = if(opts.targetArmor != SimDefaults.targetArmor) opts.targetArmor else targetArmor,
            targetType = if(opts.targetType != SimDefaults.targetType) opts.targetType else targetType,
            targetActive = if(opts.targetActive != SimDefaults.targetActive) opts.targetActive else targetActive,
            targetAutoAttackSpeedMs = if(opts.targetAutoAttackSpeedMs != SimDefaults.targetAutoAttackSpeedMs) opts.targetAutoAttackSpeedMs else targetAutoAttackSpeedMs,
            targetWeaponPower = if(opts.targetWeaponPower != SimDefaults.targetWeaponPower) opts.targetWeaponPower else targetWeaponPower,
            targetDualWield = if(opts.targetDualWield != SimDefaults.targetDualWield) opts.targetDualWield else targetDualWield,

            allowParryAndBlock = if(opts.allowParryAndBlock != SimDefaults.allowParryAndBlock) opts.allowParryAndBlock else allowParryAndBlock,
            showHiddenBuffs = if(opts.showHiddenBuffs != SimDefaults.showHiddenBuffs) opts.showHiddenBuffs else showHiddenBuffs,
            randomSeed = if(opts.randomSeed != null) opts.randomSeed else randomSeed,
        )
    }
}
