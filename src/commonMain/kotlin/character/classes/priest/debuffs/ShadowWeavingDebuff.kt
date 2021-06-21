package character.classes.priest.debuffs

import character.*
import data.Constants
import data.model.Item
import sim.Event
import sim.EventResult
import sim.EventType
import sim.SimParticipant

class ShadowWeavingDebuff(sp: SimParticipant) : Debuff(sp){
    override val name: String = "Shadow Weaving"
    override val durationMs: Int = 15000
    override val maxStacks: Int = 5

    val damagePctPerStack: Double = 0.02

    override fun stateFactory(): State {
        return Buff.State()
    }

    val state = state(sp.sim.target)

    fun shadowDamageMultiplierPct() : Double {
        return (1.0 + state.currentStacks * damagePctPerStack)
    }
}