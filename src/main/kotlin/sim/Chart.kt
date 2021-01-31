package sim

import kotlin.math.ceil
import kotlin.math.max

// A dumb-simple text chart
// Assumes the X axis is always time ordinal and that series values aren't negative
// It'll do it's best to bucket data into
object Chart {
    fun print(
        // List of x, y Pairs
        series: List<Pair<Int, Double>>,
        // Max x-axis value
        xMax: Int = series.size,
        // Max x-axis physical size
        xMaxCols: Int = xMax + 7,
        xLabel: String = "Time (s)",
        // Max y-axis value
        yMax: Int = 100,
        // Max y-axis physical size
        yMaxRows: Int = yMax.coerceAtMost(10) + 2,
        yLabel: String = "",
        bgFill: String = " ",
        pointChar: String = "*"
    ) {
        // Allow space for axes and labels
        val xMargin = 7
        val yMargin = 2

        // Rows will be printed in reverse order, so the first array is the "bottom" row
        val chart = Array(yMaxRows) { _ ->
            Array(xMaxCols) { bgFill }
        }

        // Fill axis labels and separators
        val yLabelBuffer = if(yLabel.length > yMaxRows) {
            0
        } else {
            (yMaxRows - yLabel.length) / 2
        }

        val xLabelBuffer = if(xLabel.length > xMaxCols) {
            0
        } else {
            (xMaxCols - xLabel.length) / 2
        }

        // Compute bucket sizes
        val xBucketSize: Int = ceil(xMax.toDouble() / (xMaxCols - xMargin).toDouble()).toInt()
        val yBucketSize: Int = ceil(yMax.toDouble() / (yMaxRows - yMargin).toDouble()).toInt()

        // Write axes and separators
        chart.forEachIndexed { index, row ->
            // Write Y labels
            if(index > yLabelBuffer && index <= yLabelBuffer + yLabel.length) {
                // Write a character of the y-axis label
                row[0] = yLabel[yLabel.length + yLabelBuffer - index].toString()
            }

            if(index == 2) {
                row[3] = "0"
                row[4] = "%"
            }

            if(index == yMaxRows - 1) {
                row[1] = "1"
                row[2] = "0"
                row[3] = "0"
                row[4] = "%"
            }

            // Write Y sep
            if(index != 0) {
                row[6] = "|"
            }

            // Write X label
            if(index == 0) {
                row.fill(" ")
                xLabel.forEachIndexed { index2, c ->
                    row[xLabelBuffer + index2] = c.toString()
                }
            }

            // Write X sep
            if(index == 1) {
                row.fill("-")
            }
        }

        // Fill in any sparse entries
        val sorted = series.sortedBy { it.first }

        // Write point char if there is a value within each bucket
        sorted.forEach {
            val xBucket = (it.first / xBucketSize)
            val yBucket = (it.second / yBucketSize).toInt()

            try {
               chart[yBucket + yMargin][xBucket + xMargin] = pointChar
            } catch(e: ArrayIndexOutOfBoundsException) {
                e.toString()
            }
        }

        for(line in chart.reversed()) {
            println(line.joinToString(""))
        }
    }
}
