package character.classes.priest.buffs

import character.*
import data.Constants
import data.model.Item
import sim.Event
import sim.EventResult
import sim.EventType
import sim.SimParticipant

class ShadowWeavingBuff(sp: SimParticipant) : Buff(){
    companion object {
        const val name = "Shadow Weaving"
    }
    override val id = 15258
    override val name: String = Companion.name
    override val icon: String = "spell_shadow_blackplague.jpg"
    override val durationMs: Int = 15000
    override val maxStacks: Int = 5

    override fun modifyStats(sp: SimParticipant) = Stats(
        shadowDamageMultiplier = 1 + (0.02 * state(sp).currentStacks)
    )
}
