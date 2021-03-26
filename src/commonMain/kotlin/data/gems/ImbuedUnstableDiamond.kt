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
class ImbuedUnstableDiamond : Gem(32641, "Imbued Unstable Diamond", "inv_misc_gem_diamond_06.jpg", null, Color.META, Quality.META) {
    val buff = object : Buff() {
        override val name: String = "Imbued Unstable Diamond"
        override val durationMs: Int = -1
        override val hidden: Boolean = true

        override fun modifyStats(sp: SimParticipant): Stats {
            return Stats(
                spellDamage = 14
            )
        }
    }

    override fun metaActive(sockets: List<Socket>): Boolean {
        val byColor = socketsByColor(sockets)

        return byColor[Color.YELLOW] ?: 0 >= 3
    }

    override var buffs: List<Buff> = listOf(buff)
}
