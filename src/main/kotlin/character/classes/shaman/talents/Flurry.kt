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

    val wrapper = object : Buff() {
        override val name: String = Companion.name
        override val durationMs: Int = -1
        override val hidden: Boolean = true

        override fun modifyStats(sim: SimIteration, stats: Stats): Stats {
            return stats
        }

        val onCritProc = object : Proc() {
            override val triggers: List<Trigger> = listOf(
                Trigger.MELEE_AUTO_CRIT,
                Trigger.MELEE_WHITE_CRIT,
                Trigger.MELEE_YELLOW_CRIT
            )
            override val type: Type = Type.STATIC

            val hasteBuff = object : Buff() {
                override val name: String = Companion.name
                override val durationMs: Int = 15000
                override val maxCharges: Int = 3

                // Increase melee haste for as long as we have charges
                override fun modifyStats(sim: SimIteration, stats: Stats): Stats {
                    val talentRanks = sim.subject.klass.talents[Flurry.name]?.currentRank ?: 0

                    val state = state(sim)
                    val modifier = if (talentRanks > 0 && state.currentCharges > 0) {
                        1.05 + (0.05 * talentRanks)
                    } else 1.0

                    return stats.add(
                        Stats(
                            physicalHasteMultiplier = modifier
                        )
                    )
                }

                val chargeProc = object : Proc() {
                    override val triggers: List<Trigger> = listOf(
                        Trigger.MELEE_AUTO_HIT,
                        Trigger.MELEE_WHITE_HIT,
                        Trigger.MELEE_MISS,
                        Trigger.MELEE_DODGE,
                        Trigger.MELEE_PARRY,
                        Trigger.MELEE_BLOCK,
                        Trigger.MELEE_GLANCE
                    )
                    override val type: Type = Type.STATIC

                    override fun proc(sim: SimIteration, items: List<Item>?, ability: Ability?) {
                        val state = state(sim)
                        state.currentCharges -= 1
                    }
                }

                // Proc off of melee auto hits to reduce our stacks
                override fun procs(sim: SimIteration): List<Proc> = listOf(chargeProc)
            }

            override fun proc(sim: SimIteration, items: List<Item>?, ability: Ability?) {
                sim.addBuff(hasteBuff)
            }
        }

        override fun procs(sim: SimIteration): List<Proc> = listOf(onCritProc)
    }

    override fun buffs(sim: SimIteration): List<Buff> = listOf(wrapper)
}
