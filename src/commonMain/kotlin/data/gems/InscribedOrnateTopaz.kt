package data.gems

import character.Buff
import character.Stats
import data.model.Color
import data.model.Gem
import data.model.Quality
import sim.SimParticipant
import kotlin.js.JsExport

@JsExport
class InscribedOrnateTopaz : Gem(28363, "Inscribed Ornate Topaz", "inv_misc_gem_opal_01.jpg", null, Color.ORANGE, Quality.EPIC) {
    val buff = object : Buff() {
        override val name: String = "Inscribed Ornate Topaz"
        override val durationMs: Int = -1
        override val hidden: Boolean = true

        override fun modifyStats(sp: SimParticipant): Stats {
            return Stats(
                attackPower = 10,
                rangedAttackPower = 10,
                meleeCritRating = 5.0,
                rangedCritRating = 5.0
            )
        }
    }

    override var buffs: List<Buff> = listOf(buff)
}
