package data.gems

import character.Buff
import character.Stats
import data.model.Color
import data.model.Gem
import data.model.Quality
import sim.SimParticipant
import kotlin.js.JsExport

@JsExport
class GleamingOrnateDawnstone : Gem(28120, "Gleaming Ornate Dawnstone", "inv_misc_gem_topaz_02.jpg", null, Color.YELLOW, Quality.EPIC) {
    val buff = object : Buff() {
        override val name: String = "Gleaming Ornate Dawnstone"
        override val durationMs: Int = -1
        override val hidden: Boolean = true

        override fun modifyStats(sp: SimParticipant): Stats {
            return Stats(
                spellCritRating = 10.0
            )
        }
    }

    override var buffs: List<Buff> = listOf(buff)
}
