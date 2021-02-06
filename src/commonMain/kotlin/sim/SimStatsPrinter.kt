package sim

expect object SimStatsPrinter {
    fun printAbilities(rows: List<SimStats.AbilityBreakdown>)
    fun printBuffs(title: String, rows: List<SimStats.BuffBreakdown>)
    fun printDamage(rows: List<SimStats.DamageTypeBreakdown>)
    fun printDps(mean: Double, median: Double, sd: Double)
    fun printPrecombatStats(sim: SimIteration)
}
