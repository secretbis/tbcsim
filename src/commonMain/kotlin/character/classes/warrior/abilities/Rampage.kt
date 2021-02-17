package character.classes.warrior.abilities

import character.*
import character.classes.warrior.Warrior
import character.classes.warrior.buffs.RampageBase
import data.model.Item
import sim.Event
import character.classes.warrior.talents.Rampage as RampageTalent
import sim.SimIteration

class Rampage : Ability() {
    companion object {
        const val name = "Rampage"
    }

    override val id: Int = 30033
    override val name: String = Companion.name
    override fun gcdMs(sim: SimIteration): Int = sim.physicalGcd().toInt()
    override fun cooldownMs(sim: SimIteration): Int = 0

    override fun resourceType(sim: SimIteration): Resource.Type = Resource.Type.RAGE
    override fun resourceCost(sim: SimIteration): Double = 20.0

    override fun available(sim: SimIteration): Boolean {
        val talented = sim.subject.klass.talents[RampageTalent.name]?.currentRank == 1
        val hasTriggerBuff = sim.buffs[RampageBase.rampageFlagBuff.name] != null
        return talented && hasTriggerBuff && super.available(sim)
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
            override fun proc(sim: SimIteration, items: List<Item>?, ability: Ability?, event: Event?) {
                val state = buff.state(sim)
                if (state.currentStacks < buff.maxStacks) {
                    sim.addBuff(buff)
                }
            }
        }
    }

    val stackBuff = object : Buff() {
        override val name: String = Companion.name
        override val durationMs: Int = 30000

        override val maxStacks: Int = 5

        override fun modifyStats(sim: SimIteration): Stats {
            val state = state(sim)
            return Stats(attackPower = 50 * state.currentStacks)
        }

        val proc = stackProc(this)

        override fun procs(sim: SimIteration): List<Proc> = listOf(proc)
    }

    override fun cast(sim: SimIteration) {
        sim.addBuff(stackBuff)
    }
}
