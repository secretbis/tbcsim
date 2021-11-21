package character.classes.warrior.abilities

import character.*
import character.classes.warrior.buffs.RampageBase
import data.model.Item
import sim.Event
import character.classes.warrior.talents.Rampage as RampageTalent
import sim.SimParticipant

class Rampage : Ability() {
    companion object {
        const val name = "Rampage"
    }

    override val id: Int = 30033
    override val name: String = Companion.name
    override val icon: String = "ability_warrior_rampage.jpg"
    override fun gcdMs(sp: SimParticipant): Int = sp.physicalGcd().toInt()
    override fun cooldownMs(sp: SimParticipant): Int = 0

    override fun resourceType(sp: SimParticipant): Resource.Type = Resource.Type.RAGE
    override fun resourceCost(sp: SimParticipant): Double = 20.0

    override fun available(sp: SimParticipant): Boolean {
        val talented = sp.character.klass.talents[RampageTalent.name]?.currentRank == 1
        val hasTriggerBuff = sp.buffs[RampageBase.rampageFlagBuff.name] != null
        return talented && hasTriggerBuff && super.available(sp)
    }

    // Stack rampage after the buff is initially active
    val stackProc = fun(buff: Buff): Proc {
        return object : Proc() {
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

            // TODO: Stack proc chance unconfirmed - seems to be any successful hit from testing
            override val type: Type = Type.STATIC

            // TODO: As far as I can tell from TBC footage, this won't refresh again from a proc after you get 5 stacks.
            //       Have to use Rampage again to maintain.  In pserver footage, this seems to sometimes refresh at 5.
            override fun proc(sp: SimParticipant, items: List<Item>?, ability: Ability?, event: Event?) {
                val state = buff.state(sp)
                if (state.currentStacks < buff.maxStacks) {
                    sp.addBuff(buff)
                }
            }
        }
    }

    val stackBuff = object : Buff() {
        override val name: String = "Rampage"
        override val icon: String = "ability_warrior_rampage.jpg"
        override val durationMs: Int = 30000

        override val maxStacks: Int = 5

        override fun modifyStats(sp: SimParticipant): Stats {
            val state = state(sp)
            return Stats(attackPower = 50 * state.currentStacks)
        }

        val proc = stackProc(this)

        override fun procs(sp: SimParticipant): List<Proc> = listOf(proc)
    }

    override fun cast(sp: SimParticipant) {
        sp.addBuff(stackBuff)
    }
}
