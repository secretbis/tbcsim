package data.abilities.raid

import character.*
import data.model.Item
import mechanics.Rating
import sim.Event
import sim.SimIteration

class JudgementOfWisdom : Ability() {
    companion object {
        const val name = "Judgement of Wisdom"
    }

    override val id: Int = 27164
    override val name: String = Companion.name
    override fun gcdMs(sim: SimIteration): Int = 0

    val buff = object : Buff() {
        override val name: String = "Judgement of Wisdom"
        // Assume the caster is always maintaining this
        override val durationMs: Int = -1
        override val hidden: Boolean = true

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
                Trigger.SPELL_HIT,
                Trigger.SPELL_CRIT
            )
            override val type: Type = Type.PERCENT
            override fun percentChance(sim: SimIteration): Double = 50.0

            override fun proc(sim: SimIteration, items: List<Item>?, ability: Ability?, event: Event?) {
                if(sim.subject.klass.resourceType == Resource.Type.MANA) {
                    sim.addResource(74, Resource.Type.MANA)
                }
            }
        }

        override fun procs(sim: SimIteration): List<Proc> = listOf(proc)
    }

    override fun cast(sim: SimIteration) {
        sim.addBuff(buff)
    }
}
