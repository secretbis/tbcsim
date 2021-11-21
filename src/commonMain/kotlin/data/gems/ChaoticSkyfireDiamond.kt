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
        override val icon: String = "inv_misc_gem_diamond_05.jpg"
        override val durationMs: Int = -1
        override val hidden: Boolean = true

        // TODO: on my quest for critdmg calculation i found out this critMultiplier applies to the full dmg of the crit, not just the bonus damage. this has to be applied to the base critMultiplier (Stats.physicalCritMultiplier etc.) before handling multipliers
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
