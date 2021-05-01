package character.classes.rogue.talents

import character.*
import sim.SimParticipant
import data.model.Item
import sim.Event

// TODO: need to find a better way for this

class DirtyDeeds(currentRank: Int) : Talent(currentRank) {
    companion object {
        const val name = "Dirty Deeds"
    }

    override val name: String = Companion.name
    override val maxRank: Int = 2

    fun energyReduction(): Double {
        return currentRank * 10.0
    }

    fun damageIncreasePercentExecute(): Double {
        return currentRank * 10.0
    }

    val executeDmgBuff = object : Buff() {
        override val name: String = "${Companion.name} (Execute Damage)"
        override val durationMs: Int = -1
        override val hidden: Boolean = true

        override fun modifyStats(sp: SimParticipant): Stats {  
            return Stats(
                yellowDamageMultiplier = damageIncreasePercentExecute()
            )
        }
    }

    val buff = object : Buff() {
        override val name: String = "${Companion.name} (Talent)"
        override val durationMs: Int = -1
        override val hidden: Boolean = true

        var appliedExecuteBonusBuff = false

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

            override fun proc(sp: SimParticipant, items: List<Item>?, ability: Ability?, event: Event?) {
                if(!appliedExecuteBonusBuff) {
                    sp.addBuff(executeDmgBuff)
                    appliedExecuteBonusBuff = true
                }
            }
        }

        override fun procs(sp: SimParticipant): List<Proc> = listOf(proc)
    }

    override fun buffs(sp: SimParticipant): List<Buff> = listOf(buff)
}