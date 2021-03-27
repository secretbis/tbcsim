package data.gems

import character.Ability
import character.Buff
import character.Proc
import character.Stats
import data.model.Color
import data.model.Gem
import data.model.Item
import data.model.Quality
import sim.Event
import sim.SimParticipant
import kotlin.js.JsExport

@JsExport
class ThunderingSkyfireDiamond : Gem(32410,"Thundering Skyfire Diamond", "inv_misc_gem_diamond_07.jpg", null, Color.META, Quality.META) {

    val hasteBuff = object : Buff() {
        override val name: String = "Thundering Skyfire Diamond"
        override val durationMs: Int = 6000
        //https://tbc.wowhead.com/spell=39959/skyfire-swiftness 6 seconds in 2.5.1 data
        override fun modifyStats(sp: SimParticipant): Stats {
            return Stats(physicalHasteRating = 240.0)
        }
    }

    val buff = object : Buff() {
        override val name: String = "Thundering Skyfire Diamond (passive)"
        override val durationMs: Int = -1
        override val hidden: Boolean = true

        override fun modifyStats(sp: SimParticipant): Stats {
            return stats
        }

        val proc = object : Proc() {
            override val triggers: List<Trigger> = listOf(
                Trigger.MELEE_AUTO_HIT,
                Trigger.MELEE_AUTO_CRIT,
                Trigger.MELEE_WHITE_HIT,
                Trigger.MELEE_WHITE_CRIT,
                Trigger.MELEE_YELLOW_HIT,
                Trigger.MELEE_YELLOW_CRIT,
                Trigger.MELEE_BLOCK,
                Trigger.MELEE_GLANCE
            )

            // https://70.wowfan.net/en/?spell=39958
            override val type: Type = Type.PPM
            override val ppm: Double = 0.7
            override fun cooldownMs(sp: SimParticipant): Int = 40000

            override fun proc(sp: SimParticipant, items: List<Item>?, ability: Ability?, event: Event?) {
                sp.addBuff(hasteBuff)
            }
        }

        override fun procs(sp: SimParticipant): List<Proc> = listOf(proc)
    }

    override var buffs: List<Buff> = listOf(buff)
}
