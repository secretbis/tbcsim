package data.buffs

import character.Ability
import character.Buff
import character.Proc
import character.Stats
import data.model.Item
import sim.Event
import sim.SimParticipant

class AshtongueTalismanOfSwiftness : Buff() {
    companion object {
        const val name = "Ashtongue Talisman of Swiftness"
    }

    override val name: String = "${Companion.name} (static)"
    override val icon: String = "inv_jewelry_talisman_09.jpg"
    override val durationMs: Int = -1
    override val hidden: Boolean = true

    val buff = object : Buff() {
        override val name: String = Companion.name
        override val icon: String = "inv_jewelry_talisman_09.jpg"
        override val durationMs: Int = 8000

        override fun modifyStats(sp: SimParticipant): Stats {
            return Stats(
                attackPower = 275,
                rangedAttackPower = 275
            )
        }
    }

    val proc = object : Proc() {
        override val triggers: List<Trigger> = listOf(Trigger.HUNTER_CAST_STEADY_SHOT)
        override val type: Type = Type.PERCENT
        override fun percentChance(sp: SimParticipant): Double = 15.0

        override fun proc(sp: SimParticipant, items: List<Item>?, ability: Ability?, event: Event?) {
            sp.addBuff(buff)
        }
    }

    override fun procs(sp: SimParticipant): List<Proc> = listOf(proc)
}
