package character.classes.rogue.buffs

import character.*
import character.classes.rogue.abilities.*
import data.Constants
import sim.Event
import sim.SimParticipant
import data.model.Item

class Stealth : Buff() {
    companion object {
        const val name = "Stealth"
    }

    override val name: String = Companion.name
    override val durationMs: Int = -1
    override val hidden: Boolean = true

    val stealthAbilities: Set<String> = setOf(
        Garrote.name,
        Ambush.name
    )

    val proc = makeProc(this)

    fun makeProc(buff: Buff): Proc {
        return object : Proc() {
            override val triggers: List<Trigger> = listOf(
                Trigger.MELEE_YELLOW_HIT,
                Trigger.MELEE_YELLOW_CRIT,
                Trigger.MELEE_MISS,
                Trigger.MELEE_DODGE,
                Trigger.MELEE_PARRY

                //Trigger.SPELL_CAST  // could use this and just exclude things that don't break stealth, would have to explude all precombat stuff though
            )
            override val type: Type = Type.STATIC

            override fun shouldProc(sp: SimParticipant, items: List<Item>?, ability: Ability?, event: Event?): Boolean {
                // lets just assume the rotation makes sure no other abilities are cast when stealth is on.
                // so we just remove it when a stealth ability is used.
                return (stealthAbilities.contains(ability?.name)) && super.shouldProc(sp, items, ability, event)
            }
        
            override fun proc(sp: SimParticipant, items: List<Item>?, ability: Ability?, event: Event?) {
                sp.consumeBuff(buff)
            }
        }
    }

    override fun procs(sp: SimParticipant): List<Proc> = listOf(proc)
}
