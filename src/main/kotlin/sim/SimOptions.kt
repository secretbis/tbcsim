package sim

data class SimOptions(
    var durationMs: Int = Defaults.durationMs,
    var stepMs: Int = Defaults.stepMs,
    var latencyMs: Int = Defaults.latencyMs,
    var iterations: Int = Defaults.iterations,
    var targetLevel: Int = Defaults.targetLevel,
    var targetArmor: Int = Defaults.targetArmor,
    var allowParryAndBlock: Boolean = Defaults.allowParryAndBlock
) {
   class Defaults {
       companion object {
           var durationMs: Int = 180000
           var stepMs: Int = 1
           var latencyMs: Int = 0
           var iterations: Int = 1000
           var targetLevel: Int = 73
           var targetArmor: Int = 7700
           var allowParryAndBlock: Boolean = false
       }
   }
}
