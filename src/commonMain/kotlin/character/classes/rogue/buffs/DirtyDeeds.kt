package character.classes.rogue.buffs

import character.*
import character.classes.rogue.abilities.*
import character.classes.rogue.talents.*
import data.Constants
import sim.Event
import sim.SimParticipant
import data.model.Item

class DirtyDeeds() : Buff() {
    companion object {
        const val name = "Dirty Deeds"
    }

    class DirtyDeedsState : Buff.State() {
        var executeBuffApplied: Boolean = false
    }

    override fun stateFactory(): DirtyDeedsState {
        return DirtyDeedsState()
    }

    override val name: String = "${Companion.name} (Talent) (Buff applier)"
    override val durationMs: Int = -1
    override val hidden: Boolean = true

    private fun buffState(sp: SimParticipant): DirtyDeedsState {
        return this.state(sp, name) as DirtyDeedsState
    }

    val executeDmgBuff = object : Buff() {
        override val name: String = "${Companion.name} (Execute Damage)"
        override val durationMs: Int = -1
        override val hidden: Boolean = true

        override fun modifyStats(sp: SimParticipant): Stats {  
            val dd = sp.character.klass.talents[character.classes.rogue.talents.DirtyDeeds.name] as character.classes.rogue.talents.DirtyDeeds?
            val increasedDmg = dd?.damageIncreaseMultiplierExecute() ?: 1.0

            return Stats(
                yellowDamageMultiplier = increasedDmg
            )
        }
    }

    val proc = object : Proc() {
        override val triggers: List<Trigger> = listOf(
            Trigger.MELEE_AUTO_HIT,
            Trigger.MELEE_AUTO_CRIT,
            Trigger.MELEE_WHITE_HIT,
            Trigger.MELEE_WHITE_CRIT,
            Trigger.MELEE_YELLOW_HIT,
            Trigger.MELEE_YELLOW_CRIT,
            Trigger.MELEE_MISS,
            Trigger.MELEE_DODGE,
            Trigger.MELEE_PARRY,
            Trigger.MELEE_BLOCK,
            Trigger.MELEE_GLANCE,
            Trigger.MELEE_REPLACED_AUTO_ATTACK_HIT,
            Trigger.MELEE_REPLACED_AUTO_ATTACK_CRIT,
            Trigger.SPELL_HIT,
            Trigger.SPELL_CRIT,
            Trigger.SPELL_RESIST,
            Trigger.SPELL_CAST,
            Trigger.PHYSICAL_DAMAGE,
            Trigger.SERVER_TICK,
            Trigger.SERVER_SLOW_TICK,
            Trigger.ROGUE_CAST_FINISHER,
        )

        override val type: Type = Type.STATIC

        override fun shouldProc(sp: SimParticipant, items: List<Item>?, ability: Ability?, event: Event?): Boolean {
            val state = buffState(sp)
            val isExecute = sp.sim.isExecutePhase(35.0)
            val alreadyApplied = state.executeBuffApplied
            var shouldProc = false
            if (isExecute && !alreadyApplied) {
                state.executeBuffApplied = true
                shouldProc = true
            }
            return shouldProc && super.shouldProc(sp, items, ability, event)
        }

        override fun proc(sp: SimParticipant, items: List<Item>?, ability: Ability?, event: Event?) {
            sp.addBuff(executeDmgBuff)
        }
    }

    override fun procs(sp: SimParticipant): List<Proc> = listOf(proc)
}