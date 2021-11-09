package data.buffs

import character.Ability
import character.Buff
import character.Proc
import character.Stats
import data.model.Item
import sim.Event
import sim.SimParticipant

class AshtongueTalismanOfValor : Buff() {
    companion object {
        const val name = "Ashtongue Talisman of Valor"
    }

    override val name: String = "${Companion.name} (static)"
    override val durationMs: Int = -1
    override val hidden: Boolean = true

    val buff = object : Buff() {
        override val name: String = Companion.name
        override val durationMs: Int = 12000

        override fun modifyStats(sp: SimParticipant): Stats {
            return Stats(
                strength = 55
            )
        }
    }

    val proc = object : Proc() {
        override val triggers: List<Trigger> = listOf(
            Trigger.WARRIOR_CAST_BLOODTHIRST,
            Trigger.WARRIOR_CAST_MORTAL_STRIKE,
            Trigger.WARRIOR_CAST_SHIELD_SLAM
        )
        override val type: Type = Type.PERCENT
        override fun percentChance(sp: SimParticipant): Double = 25.0

        override fun proc(sp: SimParticipant, items: List<Item>?, ability: Ability?, event: Event?) {
            sp.addBuff(buff)
        }
    }

    override fun procs(sp: SimParticipant): List<Proc> = listOf(proc)
}
