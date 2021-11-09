package data.buffs

import character.Ability
import character.Buff
import character.ItemProc
import character.Proc
import data.Constants
import data.model.Item
import sim.Event
import sim.EventResult
import sim.EventType
import sim.SimParticipant

class SyphonOfTheNathrezim(val sourceItem: Item) : Buff() {
    companion object {
        const val name = "Siphon Essence"
    }

    override val name: String = "${Companion.name} (static)"
    override val durationMs: Int = -1
    override val hidden: Boolean = true

    // TODO: With two Syphons, oes this buff stack independently or just refresh itself?
    val buff = object : Buff() {
        override val name: String = Companion.name
        override val durationMs: Int = 6000

        // Always steals 20 life from the enemy on every attack
        val siphonProc = object : Proc() {
            override val triggers: List<Trigger> = listOf(
                Trigger.MELEE_AUTO_HIT,
                Trigger.MELEE_AUTO_CRIT,
                Trigger.MELEE_WHITE_HIT,
                Trigger.MELEE_WHITE_CRIT,
                Trigger.MELEE_YELLOW_HIT,
                Trigger.MELEE_YELLOW_CRIT,
                Trigger.MELEE_BLOCK,
            )
            override val type: Type = Type.STATIC

            override fun proc(sp: SimParticipant, items: List<Item>?, ability: Ability?, event: Event?) {
                sp.logEvent(Event(
                    eventType = EventType.DAMAGE,
                    damageType = Constants.DamageType.SHADOW,
                    abilityName = Companion.name,
                    result = EventResult.HIT,
                    amount = 20.0,
                ))
            }
        }

        override fun procs(sp: SimParticipant): List<Proc> = listOf(siphonProc)
    }

    val proc = object : ItemProc(listOf(sourceItem)) {
        override val triggers: List<Trigger> = listOf(
            Trigger.MELEE_AUTO_HIT,
            Trigger.MELEE_AUTO_CRIT,
            Trigger.MELEE_WHITE_HIT,
            Trigger.MELEE_WHITE_CRIT,
            Trigger.MELEE_YELLOW_HIT,
            Trigger.MELEE_YELLOW_CRIT,
            Trigger.MELEE_BLOCK,
        )
        override val type: Type = Type.PPM
        // TODO: Proc rate not confirmed
        override val ppm: Double = 1.0

        override fun proc(sp: SimParticipant, items: List<Item>?, ability: Ability?, event: Event?) {
            sp.addBuff(buff)
        }
    }

    override fun procs(sp: SimParticipant): List<Proc> = listOf(proc)
}
