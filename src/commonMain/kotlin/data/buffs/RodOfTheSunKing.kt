package data.buffs

import character.*
import character.classes.rogue.Rogue
import character.classes.warrior.Warrior
import data.model.Item
import sim.Event
import sim.SimParticipant

class RodOfTheSunKing(val sourceItem: Item) : ItemBuff(listOf(sourceItem)) {
    override val id: Int = 36070
    override val name: String = "Rod of the Sun King (static)"
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
        // TODO: Proc rate not confirmed
        override val ppm: Double = 2.0

        override fun proc(sp: SimParticipant, items: List<Item>?, ability: Ability?, event: Event?) {
            if(sp.character.klass is Rogue) {
                sp.addResource(10, Resource.Type.ENERGY, "Rod of the Sun King")
            } else if(sp.character.klass is Warrior) {
                sp.addResource(5, Resource.Type.RAGE, "Rod of the Sun King")
            }
        }
    }

    override fun procs(sp: SimParticipant): List<Proc> = listOf(proc)
}
