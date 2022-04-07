package data.buffs

import character.*
import data.model.Item
import sim.Event
import sim.SimParticipant

class ElementalStrength : Buff() {
    companion object {
        const val name = "Elemental Strength"
    }

    override val name: String = "${Companion.name} (static)"
    override val icon: String = "spell_nature_earthquake.jpg"
    override val durationMs: Int = -1
    override val hidden: Boolean = true

    val buff = object : Buff() {
        override val name: String = Companion.name
        override val durationMs: Int = 10000
        override val icon: String = "spell_nature_earthquake.jpg"

        override fun modifyStats(sp: SimParticipant): Stats {
            return Stats(
                attackPower = 110
            )
        }
    }

    val proc = object : Proc() {
        override val triggers: List<Trigger> = listOf(Trigger.SHAMAN_CAST_SHOCK)
        override val type: Type = Type.PERCENT
        override fun percentChance(sp: SimParticipant): Double = 50.0
        override fun cooldownMs(sp: SimParticipant): Int = 10000

        override fun proc(sp: SimParticipant, items: List<Item>?, ability: Ability?, event: Event?) {
            sp.addBuff(buff)
        }
    }

    override fun procs(sp: SimParticipant): List<Proc> = listOf(proc)
}
