package sim

data class SimOptions(
    var durationMs: Int = 120000,
    var stepMs: Int = 10,
    var iterations: Int = 1000
)