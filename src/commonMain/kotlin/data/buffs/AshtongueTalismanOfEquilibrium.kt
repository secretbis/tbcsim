package data.buffs

import character.*
import data.model.Item
import sim.Event
import sim.SimParticipant

class AshtongueTalismanOfEquilibrium : Buff() {
    companion object {
        const val name = "AshtongueTalismanOfEquilibrium"
    }

    override val name: String = "${Companion.name} (static)"
    override val durationMs: Int = -1
    override val hidden: Boolean = true

    val mangleBuff = object : Buff() {
        override val name: String = Companion.name
        override val durationMs: Int = 8000

        override fun modifyStats(sp: SimParticipant): Stats {
            return Stats(
                strength = 140
            )
        }
    }

    val mangleProc = object : Proc() {
        override val triggers: List<Trigger> = listOf(Trigger.DRUID_CAST_MANGLE)
        override val type: Type = Type.PERCENT
        override fun percentChance(sp: SimParticipant): Double = 40.0

        override fun proc(sp: SimParticipant, items: List<Item>?, ability: Ability?, event: Event?) {
            sp.addBuff(mangleBuff)
        }
    }

    val starfireBuff = object : Buff() {
        override val name: String = Companion.name
        override val durationMs: Int = 8000

        override fun modifyStats(sp: SimParticipant): Stats {
            return Stats(
                spellDamage = 150
            )
        }
    }

    val starfireProc = object : Proc() {
        override val triggers: List<Trigger> = listOf(Trigger.DRUID_CAST_STARFIRE)
        override val type: Type = Type.PERCENT
        override fun percentChance(sp: SimParticipant): Double = 25.0

        override fun proc(sp: SimParticipant, items: List<Item>?, ability: Ability?, event: Event?) {
            sp.addBuff(starfireBuff)
        }
    }

    override fun procs(sp: SimParticipant): List<Proc> = listOf(mangleProc, starfireProc)
}
