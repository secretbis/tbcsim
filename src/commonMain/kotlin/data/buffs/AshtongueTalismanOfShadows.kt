package data.buffs

import character.Ability
import character.Buff
import character.Proc
import character.Stats
import data.model.Item
import sim.Event
import sim.SimParticipant

class AshtongueTalismanOfShadows : Buff() {
    companion object {
        const val name = "Ashtongue Talisman of Shadows"
    }

    override val name: String = "${Companion.name} (static)"
    override val icon: String = "inv_qirajidol_night.jpg"
    override val durationMs: Int = -1
    override val hidden: Boolean = true

    val buff = object : Buff() {
        override val name: String = Companion.name
        override val icon: String = "inv_qirajidol_night.jpg"
        override val durationMs: Int = 5000

        override fun modifyStats(sp: SimParticipant): Stats {
            return Stats(
                spellDamage = 220
            )
        }
    }

    val proc = object : Proc() {
        override val triggers: List<Trigger> = listOf(Trigger.WARLOCK_TICK_CORRUPTION)
        override val type: Type = Type.PERCENT
        override fun percentChance(sp: SimParticipant): Double = 20.0

        override fun proc(sp: SimParticipant, items: List<Item>?, ability: Ability?, event: Event?) {
            sp.addBuff(buff)
        }
    }

    override fun procs(sp: SimParticipant): List<Proc> = listOf(proc)
}
