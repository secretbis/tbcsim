package data.gems

import character.Ability
import character.Buff
import character.Proc
import character.Stats
import data.model.*
import sim.Event
import sim.SimParticipant
import kotlin.js.JsExport

@JsExport
class MysticalSkyfireDiamond : Gem(25893, "Mystical Skyfire Diamond", "inv_misc_gem_diamond_07.jpg", null, Color.META, Quality.META) {

    val hasteBuff = object : Buff() {
        override val name: String = "Mystical Skyfire Diamond"
        override val durationMs: Int = 4000

        override fun modifyStats(sp: SimParticipant): Stats {
            return Stats(spellHasteRating = 320.0)
        }
    }

    val buff = object : Buff() {
        override val name: String = "Mystical Skyfire Diamond (passive)"
        override val durationMs: Int = -1
        override val hidden: Boolean = true

        override fun modifyStats(sp: SimParticipant): Stats {
            return stats
        }

        val proc = object : Proc() {
            override val triggers: List<Trigger> = listOf(
                Trigger.SPELL_CAST
            )

            // https://70.wowfan.net/en/?spell=32837
            // wowfan has 45 ICD, patch 2.5.1 has 35 ICD
            override val type: Type = Type.PERCENT
            override fun percentChance(sp: SimParticipant): Double = 15.0
            override fun cooldownMs(sp: SimParticipant): Int = 35000

            override fun proc(sp: SimParticipant, items: List<Item>?, ability: Ability?, event: Event?) {
                sp.addBuff(hasteBuff)
            }
        }

        override fun procs(sp: SimParticipant): List<Proc> = listOf(proc)
    }

    override fun metaActive(sockets: List<Socket>): Boolean {
        val byColor = socketsByColor(sockets)

        return byColor[Color.BLUE] ?: 0 >
                byColor[Color.YELLOW] ?: 0
    }

    override var buffs: List<Buff> = listOf(buff)
}
