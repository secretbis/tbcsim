package data.buffs

import character.Ability
import character.Buff
import character.Proc
import character.Stats
import data.model.Item
import sim.Event
import sim.SimParticipant

class AshtongueTalismanOfAcumen : Buff() {
    companion object {
        const val name = "Ashtongue Talisman of Acumen"
    }

    override val name: String = "${Companion.name} (static)"
    override val icon: String = "inv_jewelry_necklace_27.jpg"
    override val durationMs: Int = -1
    override val hidden: Boolean = true

    val buff = object : Buff() {
        override val name: String = Companion.name
        override val icon: String = "inv_jewelry_necklace_27.jpg"
        override val durationMs: Int = 10000

        override fun modifyStats(sp: SimParticipant): Stats {
            return Stats(
                spellDamage = 220
            )
        }
    }

    val proc = object : Proc() {
        override val triggers: List<Trigger> = listOf(Trigger.PRIEST_TICK_SHADOW_WORD_PAIN)
        override val type: Type = Type.PERCENT
        override fun percentChance(sp: SimParticipant): Double = 10.0

        override fun proc(sp: SimParticipant, items: List<Item>?, ability: Ability?, event: Event?) {
            sp.addBuff(buff)
        }
    }

    override fun procs(sp: SimParticipant): List<Proc> = listOf(proc)
}
