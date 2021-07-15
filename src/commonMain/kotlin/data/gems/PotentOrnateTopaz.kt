package data.gems

import character.Buff
import character.Stats
import data.model.Color
import data.model.Gem
import data.model.Quality
import sim.SimParticipant
import kotlin.js.JsExport

@JsExport
class PotentOrnateTopaz : Gem(28123, "Potent Ornate Topaz", "inv_misc_gem_opal_02.jpg", null, Color.ORANGE, Quality.EPIC) {
    val buff = object : Buff() {
        override val name: String = "Potent Ornate Topaz"
        override val durationMs: Int = -1
        override val hidden: Boolean = true

        override fun modifyStats(sp: SimParticipant): Stats {
            return Stats(
                spellDamage = 6,
                spellCritRating = 5.0
            )
        }
    }

    override var buffs: List<Buff> = listOf(buff)
}
