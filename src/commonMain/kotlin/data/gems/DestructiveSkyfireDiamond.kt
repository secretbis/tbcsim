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
class DestructiveSkyfireDiamond : Gem(25890, "Destructive Skyfire Diamond", "inv_misc_gem_diamond_07.jpg", null, Color.META, Quality.META) {
    val buff = object : Buff() {
        override val name: String = "Destructive Skyfire Diamond"
        override val durationMs: Int = -1
        override val hidden: Boolean = true

        override fun modifyStats(sp: SimParticipant): Stats {
            return Stats(
                spellCritRating = 14.0
            )
        }
    }


    override var buffs: List<Buff> = listOf(buff)
}
