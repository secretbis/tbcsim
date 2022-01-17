package character.classes.warrior.abilities

import character.*
import character.classes.rogue.debuffs.ExposeArmor
import character.classes.warrior.talents.FocusedRage
import character.classes.warrior.talents.ImprovedSunderArmor
import data.abilities.raid.ImprovedExposeArmor
import mechanics.Melee
import sim.Event
import sim.EventResult
import sim.EventType
import sim.SimParticipant

class SunderArmor : Ability() {
    companion object {
        const val name = "Sunder Armor"
    }

    override val id: Int = 25225
    override val name: String = Companion.name
    override val icon: String = "ability_warrior_sunder.jpg"
    override fun gcdMs(sp: SimParticipant): Int = 0

    override fun resourceCost(sp: SimParticipant): Double {
        val focusedRageRanks = sp.character.klass.talents[FocusedRage.name]?.currentRank ?: 0
        val impSunderRanks = sp.character.klass.talents[ImprovedSunderArmor.name]?.currentRank ?: 0
        return 15.0 - focusedRageRanks - impSunderRanks
    }

    override fun available(sp: SimParticipant): Boolean {
        val hasImpEa = sp.sim.target.debuffs[ImprovedExposeArmor.name] != null || sp.sim.target.debuffs[ExposeArmor.name] != null
        return !hasImpEa && super.available(sp)
    }

    fun debuff(owner: SimParticipant) = object : Debuff(owner) {
        override val name: String = "Sunder Armor"
        override val icon: String = "ability_warrior_sunder.jpg"
        override val durationMs: Int = 30000
        override val hidden: Boolean = false
        override val maxStacks: Int = 5

        override val mutex: List<Mutex> = listOf(Mutex.DEBUFF_MAJOR_ARMOR)
        override fun mutexPriority(sp: SimParticipant): Map<Mutex, Int> = mapOf(
            Mutex.DEBUFF_MAJOR_ARMOR to (state(sp).currentStacks * 520)
        )

        override fun modifyStats(sp: SimParticipant): Stats {
            return Stats(armor = -1 * state(sp).currentStacks * 520)
        }
    }

    fun doSunder(sp: SimParticipant, isDevastate: Boolean = false, result: EventResult) {
        sp.sim.target.addDebuff(debuff(sp))

        // Sunder cast via Devastate only applies threat if it adds a stack
        val debuffStacks = sp.sim.target.debuffs[SunderArmor.name]?.state(sp.sim.target)?.currentStacks ?: 0
        val devastateProcsSunder = debuffStacks < 5
        if(!isDevastate || devastateProcsSunder) {
            sp.logEvent(
                Event(
                    eventType = EventType.THREAT,
                    amount = 301.5,
                    ability = this
                )
            )
        }

        // If the parent ability hit, fire procs for the sunder effect
        if((!isDevastate || devastateProcsSunder) && (result in listOf(EventResult.HIT, EventResult.CRIT, EventResult.BLOCK, EventResult.BLOCKED_CRIT))) {
            // Proc anything that can proc off a yellow hit
            val triggerTypes = when(result) {
                EventResult.HIT -> listOf(Proc.Trigger.MELEE_YELLOW_HIT, Proc.Trigger.PHYSICAL_DAMAGE)
                // A crit on the table is still a hit
                EventResult.CRIT -> listOf(Proc.Trigger.MELEE_YELLOW_HIT, Proc.Trigger.PHYSICAL_DAMAGE)
                EventResult.MISS -> listOf(Proc.Trigger.MELEE_MISS)
                EventResult.DODGE -> listOf(Proc.Trigger.MELEE_DODGE)
                EventResult.PARRY -> listOf(Proc.Trigger.MELEE_PARRY)
                EventResult.BLOCK -> listOf(Proc.Trigger.MELEE_YELLOW_HIT, Proc.Trigger.PHYSICAL_DAMAGE)
                EventResult.BLOCKED_CRIT -> listOf(Proc.Trigger.MELEE_YELLOW_CRIT, Proc.Trigger.PHYSICAL_DAMAGE)
                else -> null
            }

            if(triggerTypes != null) {
                sp.fireProc(triggerTypes, listOf(sp.character.gear.mainHand), this, null)
            }
        }
    }

    override fun cast(sp: SimParticipant) {
        val result = Melee.attackRoll(sp, 0.0, sp.character.gear.mainHand)
        doSunder(sp, false, result.second)
    }
}
