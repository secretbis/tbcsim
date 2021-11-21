package data.buffs

import character.*
import data.model.Item
import sim.Event
import sim.SimParticipant

class AshtongueTalismanOfVision : Buff() {
    companion object {
        const val name = "Ashtongue Talisman of Vision"
    }

    override val name: String = "${Companion.name} (static)"
    override val icon: String = "inv_jewelry_necklace_31.jpg"
    override val durationMs: Int = -1
    override val hidden: Boolean = true

    val talismanAbility = object : Ability() {
        override val name: String = Companion.name
        override val icon: String = "inv_jewelry_necklace_31.jpg"
    }

    val lbProc = object : Proc() {
        override val triggers: List<Trigger> = listOf(Trigger.SHAMAN_CAST_LIGHTNING_BOLT)
        override val type: Type = Type.PERCENT
        override fun percentChance(sp: SimParticipant): Double = 50.0

        override fun proc(sp: SimParticipant, items: List<Item>?, ability: Ability?, event: Event?) {
            sp.addResource(170, Resource.Type.MANA, talismanAbility)
        }
    }

    val ssBuff = object : Buff() {
        override val name: String = Companion.name
        override val icon: String = "inv_jewelry_necklace_31.jpg"
        override val durationMs: Int = 10000

        override fun modifyStats(sp: SimParticipant): Stats {
            return Stats(
                attackPower = 275,
                rangedAttackPower = 275
            )
        }
    }

    val ssProc = object : Proc() {
        override val triggers: List<Trigger> = listOf(Trigger.SHAMAN_CAST_STORMSTRIKE)
        override val type: Type = Type.PERCENT
        override fun percentChance(sp: SimParticipant): Double = 50.0

        override fun proc(sp: SimParticipant, items: List<Item>?, ability: Ability?, event: Event?) {
            sp.addBuff(ssBuff)
        }
    }

    override fun procs(sp: SimParticipant): List<Proc> = listOf(lbProc, ssProc)
}
