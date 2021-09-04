package character.classes.priest.talents

import character.*
import character.classes.priest.debuffs.ShadowWeavingDebuff
import character.classes.priest.debuffs.MindFlayDot
import data.Constants
import data.model.Item
import sim.Event
import sim.EventResult
import sim.EventType
import sim.SimParticipant

class MindFlay(currentRank: Int) : Talent(currentRank) {
    companion object {
        const val name = "Mind Flay"
    }
    override val name: String = Companion.name
    override val maxRank: Int = 1

    val interruptWatch = object : Buff(){
        override val name = "${Companion.name} (Static)"
        override val hidden = true
        override val durationMs = -1

        // In general only the GCD prevents another spell from being cast for channeled spells
        // When another spell is cast, mind flay "dot" should stop ticking
        val interruptProc = object : Proc() {
            override val triggers: List<Trigger> = listOf(
                Trigger.SPELL_START_CAST,
                Trigger.SPELL_CAST
            )
            override val type: Type = Type.STATIC
    
            override fun proc(sp: SimParticipant, items: List<Item>?, ability: Ability?, event: Event?) {
                // Mind Flay will refresh the dot if cast again, so we skip removing it
                if (ability == null || ability.name.startsWith("Mind Flay")) return

                val dot = sp.sim.target.debuffs.get(MindFlayDot.name)

                if (dot != null ){
                    sp.sim.target.consumeDebuff(dot);
                }
            }
        }

        override fun procs(sp: SimParticipant): List<Proc> = listOf(interruptProc)
    }

    override fun buffs(sp: SimParticipant): List<Buff> = listOf(interruptWatch)
}
