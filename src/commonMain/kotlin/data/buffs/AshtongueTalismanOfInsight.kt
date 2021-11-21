package data.buffs

import character.Ability
import character.Buff
import character.Proc
import character.Stats
import data.model.Item
import sim.Event
import sim.SimParticipant

class AshtongueTalismanOfInsight : Buff() {
    companion object {
        const val name = "Ashtongue Talisman of Insight"
    }

    override val name: String = "${Companion.name} (static)"
    override val icon: String = "inv_misc_elvencoins.jpg"
    override val durationMs: Int = -1
    override val hidden: Boolean = true

    val buff = object : Buff() {
        override val name: String = Companion.name
        override val icon: String = "inv_misc_elvencoins.jpg"
        override val durationMs: Int = 5000

        override fun modifyStats(sp: SimParticipant): Stats {
            return Stats(
                spellHasteRating = 145.0
            )
        }
    }

    val proc = object : Proc() {
        override val triggers: List<Trigger> = listOf(Trigger.SPELL_CRIT)
        override val type: Type = Type.PERCENT
        override fun percentChance(sp: SimParticipant): Double = 50.0

        override fun proc(sp: SimParticipant, items: List<Item>?, ability: Ability?, event: Event?) {
            sp.addBuff(buff)
        }
    }

    override fun procs(sp: SimParticipant): List<Proc> = listOf(proc)
}
