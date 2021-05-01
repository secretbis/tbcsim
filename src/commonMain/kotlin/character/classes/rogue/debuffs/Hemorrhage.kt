package character.classes.rogue.debuffs

import character.*
import data.Constants
import sim.Event
import sim.SimParticipant
import character.Proc
import data.model.Item

class Hemorrhage(owner: SimParticipant) : Debuff(owner) {
    companion object {
        const val name = "Hemorrhage"
    }

    override val name: String = Companion.name
    override val durationMs: Int = 15000

    // TODO: maybe reduce this to a number that represents something realistic with other raid members consuming charges, 
    //       otherwise the personal damage increase will be highly inflated.
    override val maxCharges = 10 - 1
    this.state().currentCharges = maxCharges

    val physicalDmgIncrease = 42.0

    // TODO: unsure if this is correct. 
    override fun modifyStats(sp: SimParticipant): Stats {  
        return Stats(
            whiteDamageFlatModifier = physicalDmgIncrease,
            yellowDamageFlatModifier = physicalDmgIncrease
        )
    }

    val proc = makeProc(this)

    fun makeProc(buff: Debuff): Proc {
        return object : Proc() {
            override val triggers: List<Trigger> = listOf(
                Proc.Trigger.PHYSICAL_DAMAGE
            )
            override val type: Type = Type.STATIC
        
            override fun proc(sp: SimParticipant, items: List<Item>?, ability: Ability?, event: Event?) {
                sp.consumeDebuff(buff)
            }
        }
    }

    override fun procs(sp: SimParticipant): List<Proc> = listOf(proc)
}
