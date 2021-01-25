package data.model

import character.Stats

class SocketBonus(
    val stats: Stats
) {
    fun modifyStats(stats: Stats): Stats {
        return stats.add(this.stats)
    }
}
