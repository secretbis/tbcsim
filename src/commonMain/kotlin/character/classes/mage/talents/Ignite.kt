package character.classes.mage.talents

import character.*
import data.Constants
import data.model.Item
import sim.Event
import sim.EventResult
import sim.EventType
import sim.SimParticipant

class Ignite(currentRank: Int) : Talent(currentRank) {
    companion object {
        const val name = "Ignite"
    }
    override val name: String = Companion.name
    override val maxRank: Int = 5

    val buff = object : Buff() {
        override val name: String = Companion.name
        override val durationMs: Int = -1
        override val hidden: Boolean = true

        inner class IgniteState : Buff.State() {
            // List of damage to damage event timestamp in ms
            var damageEvents: MutableList<Pair<Double, Int>> = mutableListOf()
        }

        val proc = object : Proc() {
            override val triggers: List<Trigger> = listOf(
                Trigger.FIRE_DAMAGE_NON_PERIODIC
            )
            override val type: Type = Type.STATIC

            fun igniteDebuff(sp: SimParticipant): Debuff = object : Debuff(sp) {
                override val name: String = Companion.name
                override val durationMs: Int = 4000
                override val tickDeltaMs: Int = 2000
                override val maxStacks: Int = 5

                val tickMultiplier = 0.08 * currentRank / 2

                override fun stateFactory(): State {
                    return IgniteState()
                }

                override fun tick(sp: SimParticipant) {
                    val state = state(sp) as IgniteState
                    // Calculate the ignite tick from the stored events
                    val igniteTickDamage = state.damageEvents.fold(0.0) { acc, evt ->
                        acc + (evt.first * tickMultiplier)
                    }

                    // Log event
                    owner.logEvent(Event(
                        eventType = EventType.DAMAGE,
                        damageType = Constants.DamageType.FIRE,
                        abilityName = Companion.name,
                        result = EventResult.HIT,
                        amount = igniteTickDamage
                    ))

                    // Prune events that have expired after this tick
                    state.damageEvents = state.damageEvents.filter { it.second > sp.sim.elapsedTimeMs - 4000 }.toMutableList()
                }
            }

            override fun proc(sp: SimParticipant, items: List<Item>?, ability: Ability?, event: Event?) {
                if(event?.result == EventResult.CRIT) {
                    val debuff = sp.sim.target.debuffs[Companion.name] ?: igniteDebuff(sp)
                    val debuffState = debuff.state(sp.sim.target) as IgniteState
                    debuffState.damageEvents.add(Pair(event.amount, sp.sim.elapsedTimeMs))
                    sp.sim.target.addDebuff(debuff)
                }
            }
        }

        override fun procs(sp: SimParticipant): List<Proc> = listOf(proc)
    }

    override fun buffs(sp: SimParticipant): List<Buff> = listOf(buff)
}
