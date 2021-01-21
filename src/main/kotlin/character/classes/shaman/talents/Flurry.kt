package character.classes.shaman.talents

import character.*
import data.model.Item
import sim.SimIteration

class Flurry(currentRank: Int) : Talent(currentRank) {
    companion object {
        const val name: String = "Flurry"
    }

    override val name: String = Companion.name
    override val maxRank: Int = 5

    val buff = object : Buff() {
        override val durationMs: Int = 15000
        override val hidden: Boolean = false
        override val maxCharges: Int = 3

        // Increase melee haste for as long as we have charges
        override fun modifyStats(sim: SimIteration, stats: Stats): Stats {
            val talentRanks = sim.subject.talents[Flurry.name]?.currentRank ?: 0

            val state = state(sim)
            val modifier = if(talentRanks > 0 && state.currentCharges > 0) {
                1.05 + (0.05 * talentRanks)
            } else 1.0

            return stats.add(
                Stats(
                    physicalHasteMultiplier = modifier
                )
            )
        }

        // Proc off of melee auto hits to reduce our stacks
        override val procs: List<Proc> = listOf(
            object : Proc() {
                override val triggers: List<Trigger> = listOf(
                    Trigger.MELEE_AUTO_HIT
                )

                override fun proc(sim: SimIteration, items: List<Item>?, ability: Ability?) {
                    val state = state(sim)
                    state.currentCharges -= 1
                }
            }
        )
    }

    override val procs: List<Proc> = listOf(
        object : Proc() {
            override val triggers: List<Trigger> = listOf(
                Trigger.MELEE_AUTO_CRIT,
                Trigger.MELEE_WHITE_CRIT,
                Trigger.MELEE_YELLOW_CRIT
            )

            override fun proc(sim: SimIteration, items: List<Item>?, ability: Ability?) {
                sim.addBuff(buff)
            }
        }
    )
}
