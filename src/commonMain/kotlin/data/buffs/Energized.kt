package data.buffs

import character.*
import data.model.Item
import sim.Event
import sim.SimParticipant

class Energized : Buff() {
    companion object {
        const val name = "Energized"
    }

    override val name: String = "${Companion.name} (static)"
    override val icon: String = "spell_nature_callstorm.jpg"
    override val durationMs: Int = -1
    override val hidden: Boolean = true

    val buff = object : Buff() {
        override val name: String = Companion.name
        override val durationMs: Int = 10000
        override val icon: String = "spell_nature_callstorm.jpg"

        override fun modifyStats(sp: SimParticipant): Stats {
            return Stats(
                spellHasteRating = 110.0
            )
        }
    }

    val proc = object : Proc() {
        override val triggers: List<Trigger> = listOf(Trigger.SHAMAN_CAST_LIGHTNING_BOLT)
        override val type: Type = Type.PERCENT
        override fun percentChance(sp: SimParticipant): Double = 15.0

        override fun proc(sp: SimParticipant, items: List<Item>?, ability: Ability?, event: Event?) {
            sp.addBuff(buff)
        }
    }

    override fun procs(sp: SimParticipant): List<Proc> = listOf(proc)
}
