package data.buffs

import character.*
import data.Constants
import data.model.Item
import mechanics.Melee
import sim.Event
import sim.EventType
import sim.SimParticipant

class Despair(val sourceItem: Item) : ItemBuff(listOf(sourceItem)) {
    override val name: String = "Despair (static)"
    override val durationMs: Int = -1
    override val hidden: Boolean = true

        val procAbility = object : Ability() {
            override val id: Int = 34580
            override val name: String = "Despair"
            override fun gcdMs(sp: SimParticipant): Int = 0
            override fun cast(sp: SimParticipant) {
                val item = sp.character.gear.mainHand
                val result = Melee.attackRoll(sp, 600.0, item , isWhiteDmg = false)

                val damageEvent = Event(
                    eventType = EventType.DAMAGE,
                    damageType = Constants.DamageType.PHYSICAL_IGNORE_ARMOR,
                    abilityName = name,
                    amount = result.first,
                    result = result.second,
                )
                sp.logEvent(damageEvent)
            }
        }

        val proc = object : ItemProc(listOf(sourceItem)) {
            override val triggers: List<Trigger> = listOf(
                Trigger.MELEE_AUTO_HIT,
                Trigger.MELEE_AUTO_CRIT,
                Trigger.MELEE_WHITE_HIT,
                Trigger.MELEE_WHITE_CRIT,
                Trigger.MELEE_YELLOW_HIT,
                Trigger.MELEE_YELLOW_CRIT,
                Trigger.MELEE_GLANCE,
                Trigger.MELEE_BLOCK
            )
            override val type: Type = Type.PPM
            override val ppm: Double = 0.5
            //based on wowhead comments (approximation) TODO: check live procrate

            override fun proc(sp: SimParticipant, items: List<Item>?, ability: Ability?, event: Event?) {
                procAbility.cast(sp)
            }
        }

        override fun procs(sp: SimParticipant): List<Proc> = listOf(proc)
}
