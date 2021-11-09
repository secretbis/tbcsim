package data.buffs

import character.*
import data.Constants
import data.model.Item
import sim.Event
import sim.EventResult
import sim.EventType
import sim.SimParticipant

class AshtongueTalismanOfZeal : Buff() {
    companion object {
        const val name = "Ashtongue Talisman of Zeal"
    }

    override val name: String = "${Companion.name} (static)"
    override val durationMs: Int = -1
    override val hidden: Boolean = true

    fun debuff(sp: SimParticipant) = object : Debuff(sp) {
        override val name: String = Companion.name
        override val durationMs: Int = 8000
        override val tickDeltaMs: Int = 2000

        val baseDmg = 480
        val numTicks = durationMs / tickDeltaMs
        override fun tick(sp: SimParticipant) {
            owner.logEvent(Event(
                eventType = EventType.DAMAGE,
                damageType = Constants.DamageType.PHYSICAL,
                abilityName = Companion.name,
                result = EventResult.HIT,
                amount = (baseDmg / numTicks).toDouble(),
            ))
        }
    }

    val proc = object : Proc() {
        override val triggers: List<Trigger> = listOf(Trigger.PALADIN_CAST_JUDGEMENT)
        override val type: Type = Type.PERCENT
        override fun percentChance(sp: SimParticipant): Double = 50.0

        override fun proc(sp: SimParticipant, items: List<Item>?, ability: Ability?, event: Event?) {
            sp.sim.target.addDebuff(debuff(sp))
        }
    }

    override fun procs(sp: SimParticipant): List<Proc> = listOf(proc)
}
