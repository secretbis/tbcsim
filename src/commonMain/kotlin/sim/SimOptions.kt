package sim

import kotlin.js.JsExport

@JsExport
data class SimOptions(
    var durationMs: Int = SimDefaults.durationMs,
    var durationVaribilityMs: Int = SimDefaults.durationVaribilityMs,
    var stepMs: Int = SimDefaults.stepMs,
    var latencyMs: Int = SimDefaults.latencyMs,
    var iterations: Int = SimDefaults.iterations,
    var targetLevel: Int = SimDefaults.targetLevel,
    var targetArmor: Int = SimDefaults.targetArmor,
    var allowParryAndBlock: Boolean = SimDefaults.allowParryAndBlock,
    var showHiddenBuffs: Boolean = SimDefaults.showHiddenBuffs
)
