package character.classes.shaman.talents

import character.*
import data.model.Item
import sim.SimIteration

class UnleashedRage(currentRank: Int) : Talent(currentRank) {
    companion object {
        const val name: String = "Unleashed Rage"
    }

    override val name: String = Companion.name
    override val maxRank: Int = 5

    val buff = object : Buff() {
        override val name: String = Companion.name
        override val durationMs: Int = 10000
        override val hidden: Boolean = false

        override fun modifyStats(sim: SimIteration, stats: Stats): Stats {
            val talentRanks = sim.subject.talents[UnleashedRage.name]?.currentRank ?: 0

            val modifier = 1 * (0.2 * talentRanks)
            return stats.add(
                Stats(
                    attackPowerMultiplier = modifier,
                    rangedAttackPowerMultiplier = modifier
                )
            )
        }

        override val procs: List<Proc> = listOf()
    }

    override val procs: List<Proc> = listOf(
        object : Proc() {
            override val triggers: List<Trigger> = listOf(
                Trigger.MELEE_WHITE_CRIT,
                Trigger.MELEE_YELLOW_CRIT
            )

            override fun proc(sim: SimIteration, items: List<Item>?, ability: Ability?) {
                sim.addBuff(buff)
            }
        }
    )
}
