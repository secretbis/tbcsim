package data.buffs

import character.Ability
import character.Buff
import character.Proc
import character.Stats
import data.model.Item
import sim.Event
import sim.SimParticipant

class BadgeOfTheSwarmguard : Buff() {
    companion object {
        const val name = "Badge of the Swarmguard (static)"
    }
    override val id: Int = 26480
    override val name: String = Companion.name
    override val durationMs: Int = -1
    override val hidden: Boolean = true

    val buffDurationMs = 30000
    val buff = object : Buff() {
        override val name: String  = "Badge of the Swarmguard"
        override val durationMs: Int = buffDurationMs

        val proc = object : Proc() {
            override val triggers: List<Trigger> = listOf(
                Trigger.MELEE_AUTO_HIT,
                Trigger.MELEE_AUTO_CRIT,
                Trigger.MELEE_WHITE_HIT,
                Trigger.MELEE_WHITE_CRIT,
                Trigger.MELEE_YELLOW_HIT,
                Trigger.MELEE_YELLOW_CRIT,
                Trigger.MELEE_BLOCK,
                Trigger.MELEE_GLANCE,
                Trigger.RANGED_AUTO_HIT,
                Trigger.RANGED_AUTO_CRIT,
                Trigger.RANGED_WHITE_HIT,
                Trigger.RANGED_WHITE_CRIT,
                Trigger.RANGED_YELLOW_HIT,
                Trigger.RANGED_YELLOW_CRIT,
                Trigger.RANGED_BLOCK,
            )
            override val type: Type = Type.PPM
            override val ppm: Double = 10.0
            //from fightclub discord
            override fun cooldownMs(sp: SimParticipant): Int = 0
            override fun proc(sp: SimParticipant, items: List<Item>?, ability: Ability?, event: Event?){
                val state = stackBuff.state(sp)
                if (state.currentStacks < stackBuff.maxStacks) {
                    sp.addBuff(stackBuff)
                }
            }
        }

        override fun modifyStats(sp: SimParticipant): Stats? {
            val state = stackBuff.state(sp)
            return Stats(armorPen= 200 * state.currentStacks)
        }

        override fun procs(sp: SimParticipant): List<Proc> = listOf(proc)
    }
    val stackBuff = object : Buff() {
        override val name: String = "Arp Buff"
        override val durationMs: Int = -1
        override val maxStacks: Int = 6
        override val hidden: Boolean = true
    }

    val ability = object : Ability() {
        override val id: Int = 26480
        override val name: String = "Badge of the Swarmguard"
        override fun gcdMs(sp: SimParticipant): Int = 0
        override fun cooldownMs(sp: SimParticipant): Int = 180000

       //no trinket lockout

        override fun cast(sp: SimParticipant) {
            val state = stackBuff.state(sp)
            sp.addBuff(buff)
            if(state.currentStacks > 1){
                sp.consumeBuff(stackBuff)
                //remove stacks from previous application when the trinket is used
            }
        }
    }

    override fun activeTrinketAbility(sp: SimParticipant): Ability = ability
}
