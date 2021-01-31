package data.abilities.raid

import character.Ability
import character.Buff
import character.Proc
import character.Stats
import data.model.Item
import mechanics.Rating
import sim.SimIteration

class JudgementOfWisdom : Ability() {
    companion object {
        const val name = "Leader of the Pack"
    }

    override val id: Int = 17007
    override val name: String = Companion.name
    override fun gcdMs(sim: SimIteration): Int = 0

    val buff = object : Buff() {
        override val name: String = Companion.name
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
            )
            override val type: Type = Type.PERCENT
            override val percentChance: Double = 50.0

            override fun proc(sim: SimIteration, items: List<Item>?, ability: Ability?) {
                TODO("Not yet implemented")
            }

        }

        val bonusCritRating = 5.0 * Rating.critPerPct
        override fun modifyStats(sim: SimIteration): Stats? {
            return Stats(
                physicalCritRating = bonusCritRating
            )
        }
    }

    override fun cast(sim: SimIteration, free: Boolean) {
        sim.addBuff(buff)
    }

    override val baseCastTimeMs: Int = 0
}
