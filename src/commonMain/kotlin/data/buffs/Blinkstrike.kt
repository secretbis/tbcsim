package data.buffs

import character.*
import character.Stats
import data.Constants
import data.model.Item
import mechanics.Melee
import sim.Event
import sim.EventResult
import sim.EventType
import sim.SimParticipant

class Blinkstrike (val sourceItem: Item) : ItemBuff(listOf(sourceItem)) {

    //functionality based on https://tbc.wowhead.com/item=31332/blinkstrike#comments:id=3319722
    override val id: Int = 38308
    override val name: String = "Blinkstrike (static)"
    override val durationMs: Int = -1
    override val hidden: Boolean = true

    val extraAttack = object : Ability() {
        override val id: Int = 38308
        override val name: String = "Blinkstrike Extra Attack"
        override val icon: String = "inv_sword_76.jpg"
        override fun gcdMs(sp: SimParticipant): Int = 0

        override fun cast(sp: SimParticipant) {
            val mh = sp.character.gear.mainHand
            val attack = Melee.baseDamageRoll(sp, mh)
            val result = Melee.attackRoll(sp, attack, mh, isWhiteDmg = true)

            val event = Event(
                eventType = EventType.DAMAGE,
                damageType = Constants.DamageType.PHYSICAL,
                isWhiteDamage = true,
                ability = this,
                amount = result.first,
                result = result.second,
            )
            sp.logEvent(event)

            // Proc anything that can proc off a white hit
            val triggerTypes = when (result.second) {
                EventResult.HIT -> listOf(Proc.Trigger.MELEE_WHITE_HIT, Proc.Trigger.PHYSICAL_DAMAGE)
                EventResult.CRIT -> listOf(Proc.Trigger.MELEE_WHITE_CRIT, Proc.Trigger.PHYSICAL_DAMAGE)
                else -> null
            }

            if (triggerTypes != null) {
                sp.fireProc(triggerTypes, listOf(mh), this, event)
            }
        }
    }

    val weaponProc = object : ItemProc(listOf(sourceItem)) {
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
        override val ppm: Double = 1.0
        //TODO: confirm proc rate, check that there is no reset on MH swing (unlikely as other similar procs had the swing time reset removed)

        override fun proc(sp: SimParticipant, items: List<Item>?, ability: Ability?, event: Event?) {
            extraAttack.cast(sp)
        }
    }
    override fun procs(sp: SimParticipant): List<Proc> = listOf(weaponProc)
}
