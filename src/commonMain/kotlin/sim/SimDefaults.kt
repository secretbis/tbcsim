package sim

import kotlin.js.JsExport

@JsExport
object SimDefaults {
    // The length of the fight you wish to simulate, in millseconds
    const val durationMs: Int = 180000
    // Randomly alters the fight duration by adding or subtracting a random number of milliseconds, up to the configured value
    // This helps model the real world more effectively, and can better evaluate things like haste effects or potion usage timings
    const val durationVaribilityMs: Int = 0
    const val stepMs: Int = 10
    const val latencyMs: Int = 0
    const val iterations: Int = 10000
    const val targetLevel: Int = 73
    // Per ancient forums, most TBC bosses have 7700 or 6200 armor
    const val targetArmor: Int = 7700
    const val allowParryAndBlock: Boolean = false
    const val showHiddenBuffs: Boolean = false
}
