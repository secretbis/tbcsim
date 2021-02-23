package data.model

import character.Stats
import data.Constants
import kotlin.js.JsExport

@JsExport
abstract class Gem(id: Int, name: String, icon: String, prefix: Prefix?, val color: Color, quality: Quality): Item() {
    init {
        // Convert GemStats to item stats
        val tmpStats = Stats()
        for(gemStat in prefix?.stat ?: listOf()) {
            val value = when(quality) {
                Quality.UNCOMMON -> gemStat.uncommonValue
                Quality.RARE -> gemStat.rareValue
                Quality.EPIC -> gemStat.epicValue
                Quality.META -> gemStat.metaValue
                else -> 0
            }
            tmpStats.addByStatType(gemStat.stat, value)
        }

        val cleanPrefixName = if(prefix != null) { "${prefix.name.toLowerCase().capitalize()} " } else ""

        this.stats = tmpStats
        this.quality = quality.ordinal
        this.name = "$cleanPrefixName$name"
        this.id = id
        this.icon = icon
    }

    // Meta gem color requirements
    internal fun socketsByColor(sockets: List<Socket>): Map<Color, Int> {
        // Find all gems that match red/yellow/blue, and count them
        val byColor = mutableMapOf<Color, Int>()
        byColor[Color.RED] = 0
        byColor[Color.YELLOW] = 0
        byColor[Color.BLUE] = 0

        sockets.filter { it.gem != null }.forEach {
            if(Color.matchesColor(it.gem!!, Color.RED)) {
                byColor[Color.RED] = byColor[Color.RED]!! + 1
            }
            if(Color.matchesColor(it.gem!!, Color.YELLOW)) {
                byColor[Color.YELLOW] = byColor[Color.YELLOW]!! + 1
            }
            if(Color.matchesColor(it.gem!!, Color.BLUE)) {
                byColor[Color.BLUE] = byColor[Color.BLUE]!! + 1
            }
        }

        return byColor
    }

    open fun metaActive(sockets: List<Socket>): Boolean {
        val byColor = socketsByColor(sockets)

        // Most meta gems have a requirement of at least 2 of each color
        // Ones that do not will override this
        return byColor[Color.RED] ?: 0 >= 2 &&
                byColor[Color.YELLOW] ?: 0 >= 2 &&
                byColor[Color.BLUE] ?: 0 >= 2
    }
}
