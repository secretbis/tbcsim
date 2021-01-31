package sim

import io.kotest.core.spec.style.StringSpec
import kotlin.random.Random

class ChartTest : StringSpec ({
    "should render a basic chart" {
        // Make a random chart that looks sort of like mana consumption
        var current = 100.0
        val deviation = 5.0

        val series = (0..180).map {
            // Bias down
            val delta = Random.nextDouble(-1.5 * deviation, deviation)
            current += delta
            Pair(it, current.coerceAtLeast(0.0).coerceAtMost(100.0))
        }

        Chart.print(series, yLabel = "RAGE")
    }
})
