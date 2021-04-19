package data.buffs

import character.*
import data.model.Item
import sim.Event
import sim.SimParticipant
import kotlin.random.Random

class TheHammerOfDestiny(val sourceItem: Item) : ItemBuff(listOf(sourceItem)) {
    override val id: Int = 38284
    override val name: String = "The Hammer of Destiny (static)"
    override val durationMs: Int = -1
    override val hidden: Boolean = true

    val proc = object : ItemProc(listOf(sourceItem)) {
        override val triggers: List<Trigger> = listOf(
            Trigger.MELEE_AUTO_HIT,
            Trigger.MELEE_AUTO_CRIT,
            Trigger.MELEE_WHITE_HIT,
            Trigger.MELEE_WHITE_CRIT,
            Trigger.MELEE_YELLOW_HIT,
            Trigger.MELEE_YELLOW_CRIT,
            Trigger.MELEE_BLOCK,
            Trigger.MELEE_GLANCE,
        )

        override val type: Type = Type.PPM
        override val ppm: Double = 3.0
        //TODO:Confirm proc rate, based on wowhead comment, test when ret is added
    val manaRestore = object : Ability(){
            override val id: Int = 34107
            override val name: String = "The Hammer of Destiny"
            override fun gcdMs(sp: SimParticipant): Int = 0

            override fun cast(sp: SimParticipant) {
                val manaRestored = Random.nextInt(170, 230)
                sp.addResource(manaRestored, Resource.Type.MANA, name)
            }

    }
        override fun proc(sp: SimParticipant, items: List<Item>?, ability: Ability?, event: Event?) {
            manaRestore.cast(sp)
        }
    }

    override fun procs(sp: SimParticipant): List<Proc> = listOf(proc)
}
