package sim

data class SimOptions(
    var durationMs: Int = 120000,
    var stepMs: Int = 10,
    var iterations: Int = 1000,
    var targetLevel: Int = 73,
    var targetArmor: Double = 7700.0,
    var allowParryAndBlock: Boolean = false
)
