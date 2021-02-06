package sim

actual object SimStatsPrinter {
    actual fun printAbilities(rows: List<SimStats.AbilityBreakdown>) {
    }

    actual fun printBuffs(title: String, rows: List<SimStats.BuffBreakdown>) {
    }

    actual fun printDamage(rows: List<SimStats.DamageTypeBreakdown>) {
    }

    actual fun printDps(mean: Double, median: Double, sd: Double) {
        console.log("AVERAGE DPS: $mean")
        console.log("MEDIAN DPS: $mean")
        console.log("STDDEV DPS: $mean")
    }

    actual fun printPrecombatStats(sim: SimIteration) {
    }
}
