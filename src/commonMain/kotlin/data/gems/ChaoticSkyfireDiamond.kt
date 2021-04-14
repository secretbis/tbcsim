package data.gems

import character.Buff
import character.Stats
import data.model.Color
import data.model.Gem
import data.model.Quality
import data.model.Socket
import sim.SimParticipant
import kotlin.js.JsExport

@JsExport
class ChaoticSkyfireDiamond : Gem(34220, "Chaotic Skyfire Diamond", "inv_misc_gem_diamond_07.jpg", null, Color.META, Quality.META) {
    val buff = object : Buff() {
        override val name: String = "Chaotic Skyfire Diamond"
        override val durationMs: Int = -1
        override val hidden: Boolean = true

        override fun modifyStats(sp: SimParticipant): Stats {
            return Stats(
                meleeCritRating = 12.0,
                rangedCritRating = 12.0,
                whiteDamageAddlCritMultiplier = 1.03,
                yellowDamageAddlCritMultiplier = 1.03
            )
        }
    }

    override var buffs: List<Buff> = listOf(buff)

    override fun metaActive(sockets: List<Socket>): Boolean {
        val byColor = socketsByColor(sockets)

        // Most meta gems have a requirement of at least 2 of each color
        // Ones that do not will override this
        return byColor[Color.BLUE] ?: 0 >= 2
    }
}
