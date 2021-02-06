package data.model

import character.Stats

class SocketBonus(
    val stats: Stats
) {
    fun modifyStats(): Stats {
        return this.stats
    }
}
