package sim

data class SimOptions(
    var durationMs: Int = 120000,
    var stepMs: Int = 1,
    var iterations: Int = 1000,
    var targetLevel: Int = 73,
    var targetArmor: Int = 7700,
    var allowParryAndBlock: Boolean = false
)
