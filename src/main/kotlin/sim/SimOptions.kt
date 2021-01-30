package sim

data class SimOptions(
    var durationMs: Int = Defaults.durationMs,
    var durationVaribilityMs: Int = Defaults.durationVaribilityMs,
    var stepMs: Int = Defaults.stepMs,
    var latencyMs: Int = Defaults.latencyMs,
    var iterations: Int = Defaults.iterations,
    var targetLevel: Int = Defaults.targetLevel,
    var targetArmor: Int = Defaults.targetArmor,
    var allowParryAndBlock: Boolean = Defaults.allowParryAndBlock
) {
   class Defaults {
       companion object {
           // The length of the fight you wish to simulate, in millseconds
           val durationMs: Int = 180000
           // Randomly alters the fight duration by adding or subtracting a random number of milliseconds, up to the configured value
           // This helps model the real world more effectively, and can better evaluate things like haste effects or potion usage timings
           val durationVaribilityMs: Int = 0
           val stepMs: Int = 1
           val latencyMs: Int = 0
           val iterations: Int = 1000
           val targetLevel: Int = 73
           // Per ancient forums, most TBC bosses have 7700 or 6200 armor
           val targetArmor: Int = 7700
           val allowParryAndBlock: Boolean = false
       }
   }
}
