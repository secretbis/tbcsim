package character.classes.priest.debuffs

import character.*
import data.Constants
import data.model.Item
import sim.Event
import sim.EventResult
import sim.EventType
import sim.SimParticipant

class ShadowWeavingDebuff(sp: SimParticipant) : Debuff(sp){
    override val id = 15258
    override val name: String = "Shadow Vulnerability"
    override val durationMs: Int = 15000
    override val maxStacks: Int = 5

    override fun modifyStats(sp: SimParticipant) = Stats(
        shadowDamageTakenMultiplier = 1 + (0.02 * state(sp).currentStacks)
    )
}