package character.classes.rogue.buffs

import character.*
import character.classes.rogue.abilities.*
import data.Constants
import sim.Event
import sim.SimParticipant
import data.model.Item

class ColdBlood() : Buff() {
    companion object {
        const val name = "Cold Blood"
    }

    override val name: String = Companion.name
    override val durationMs: Int = -1

    val consumingAbilities = setOf(
        SinisterStrike.name,
        Ambush.name,
        Backstab.name,
        Envenom.name,
        Eviscerate.name,
        Shiv.name,
        Mutilate.secondaryName,  // cold blood applies to both hits, make sure its only removed on the 2nd hit
        Hemorrhage.name,
        GhostlyStrike.name
    )

    // this will also make it proc on Kick and Gouge which are not supposed to consume this buff. Should not be relevant for dps sims though.
    override fun modifyStats(sp: SimParticipant): Stats {  
        return Stats(
            yellowHitsAdditionalCritPct = 100.0
        )
    }

    val proc = makeProc(this)

    fun makeProc(buff: Buff): Proc {
        return object : Proc() {
            override val triggers: List<Trigger> = listOf(
                Trigger.MELEE_YELLOW_HIT,
                Trigger.MELEE_YELLOW_CRIT,
                Trigger.MELEE_MISS,
                Trigger.MELEE_DODGE,
                Trigger.MELEE_PARRY
            )
            override val type: Type = Type.STATIC
        
            override fun shouldProc(sp: SimParticipant, items: List<Item>?, ability: Ability?, event: Event?): Boolean {
                return consumingAbilities.contains(ability?.name) && super.shouldProc(sp, items, ability, event)
            }
        
            override fun proc(sp: SimParticipant, items: List<Item>?, ability: Ability?, event: Event?) {
                sp.consumeBuff(buff)
            }
        }
    }

    override fun procs(sp: SimParticipant): List<Proc> = listOf(proc)
}