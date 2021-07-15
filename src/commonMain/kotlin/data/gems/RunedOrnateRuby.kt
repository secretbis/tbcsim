package data.gems

import character.Buff
import character.Stats
import data.model.Color
import data.model.Gem
import data.model.Quality
import sim.SimParticipant
import kotlin.js.JsExport

@JsExport
class RunedOrnateRuby : Gem(28118, "Runed Ornate Ruby", "inv_misc_gem_bloodstone_01.jpg", null, Color.RED, Quality.EPIC) {
    val buff = object : Buff() {
        override val name: String = "Runed Ornate Ruby"
        override val durationMs: Int = -1
        override val hidden: Boolean = true

        override fun modifyStats(sp: SimParticipant): Stats {
            return Stats(
                spellDamage = 12
            )
        }
    }

    override var buffs: List<Buff> = listOf(buff)
}
