package data.buffs

import character.Ability
import character.Buff
import character.Proc
import character.Stats
import data.model.Item
import sim.Event
import sim.SimParticipant

class BlackenedNaaruSliver : Buff() {
    companion object {
        const val name = "Blackened Naaru Sliver"
    }

    override val name: String = "${Companion.name} (static)"
    override val icon: String = "inv_jewelry_talisman_16.jpg"
    override val durationMs: Int = -1
    override val hidden: Boolean = true

    val apBuff = object : Buff() {
        override val name: String = Companion.name
        override val icon: String = "inv_jewelry_talisman_16.jpg"
        override val durationMs: Int = 20000
        override val maxStacks: Int = 10

        override fun modifyStats(sp: SimParticipant): Stats {
            val stacks = state(sp).currentStacks
            return Stats(
                attackPower = 44 * stacks,
                rangedAttackPower = 44 * stacks
            )
        }
    }

    val apProc = object : Proc() {
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
        override val type: Type = Type.STATIC

        override fun proc(sp: SimParticipant, items: List<Item>?, ability: Ability?, event: Event?) {
            sp.addBuff(apBuff)
        }
    }

    val battleTranceBuff = object : Buff() {
        override val name: String = "${Companion.name} (battle trance)"
        override val icon: String = "inv_jewelry_talisman_16.jpg"
        override val durationMs: Int = 20000
        override val hidden: Boolean = true

        override fun procs(sp: SimParticipant): List<Proc> = listOf(apProc)

        override fun reset(sp: SimParticipant) {
            // Remove any lingering AP buffs
            sp.removeBuffs(listOf(apBuff))
            super.reset(sp)
        }
    }

    val battleTranceProc = object : Proc() {
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
        override val type: Type = Type.PERCENT
        override fun percentChance(sp: SimParticipant): Double = 10.0
        override fun cooldownMs(sp: SimParticipant): Int = 45000

        override fun proc(sp: SimParticipant, items: List<Item>?, ability: Ability?, event: Event?) {
            sp.addBuff(battleTranceBuff)
        }
    }

    override fun procs(sp: SimParticipant): List<Proc> = listOf(battleTranceProc)
}
