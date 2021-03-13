package data.gems

import character.Buff
import character.Stats
import data.model.Color
import data.model.Gem
import data.model.Quality
import sim.SimParticipant
import kotlin.js.JsExport

@JsExport
class RelentlessEarthstormDiamond : Gem(32409, "Relentless Earthstorm Diamond", "inv_misc_gem_diamond_07.jpg", null, Color.META, Quality.META) {
    val buff = object : Buff() {
        override val name: String = "Relentless Earthstorm Diamond"
        override val durationMs: Int = -1
        override val hidden: Boolean = true

        override fun modifyStats(sp: SimParticipant): Stats {
            return Stats(
                agility = 12,
                whiteDamageAddlCritMultiplier = 1.03,
                yellowDamageAddlCritMultiplier = 1.03
            )
        }
    }

    override var buffs: List<Buff> = listOf(buff)
}
