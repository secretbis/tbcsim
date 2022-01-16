package data.gems

import character.Buff
import character.Stats
import data.model.Color
import data.model.Gem
import data.model.Quality
import sim.SimParticipant
import kotlin.js.JsExport

@JsExport
class PowerfulEarthstormDiamond : Gem(25896, "Powerful Earthstorm Diamond", "inv_misc_gem_diamond_04.jpg", null, Color.META, Quality.META) {
    val buff = object : Buff() {
        override val name: String = "Powerful Earthstorm Diamond"
        override val icon: String = "inv_misc_gem_diamond_06.jpg"
        override val durationMs: Int = -1
        override val hidden: Boolean = true

        override fun modifyStats(sp: SimParticipant): Stats {
            return Stats(
                stamina = 18,
                // TODO: Add stun resist if that is ever modeled
            )
        }
    }

    override var buffs: List<Buff> = listOf(buff)
}
