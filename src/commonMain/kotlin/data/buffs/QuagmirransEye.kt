package data.buffs

import character.Ability
import character.Buff
import character.Proc
import character.Stats
import data.model.Item
import sim.Event
import sim.SimIteration

class QuagmirransEye : Buff() {
    companion object {
        const val name = "Quagmirran's Eye"
    }

    override val name: String = Companion.name
    override val durationMs: Int = -1
    override val hidden: Boolean = true

    val buff = object : Buff() {
        override val id: Int = 33297
        override val name: String = Companion.name
        override val durationMs: Int = 6000

        override fun modifyStats(sim: SimIteration): Stats? {
            return Stats(spellHasteRating = 320.0)
        }
    }

    val proc = object : Proc() {
        // TODO: Does this trigger on DoTs
        override val triggers: List<Trigger> = listOf(
            Trigger.SPELL_HIT,
            Trigger.SPELL_CRIT
        )
        override val type: Type = Type.PERCENT
        override fun percentChance(sim: SimIteration): Double = 10.0
        override fun cooldownMs(sim: SimIteration): Int = 45000

        override fun proc(sim: SimIteration, items: List<Item>?, ability: Ability?, event: Event?) {
            sim.addBuff(buff)
        }
    }
}
