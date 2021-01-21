package character.classes.shaman.talents

import character.Buff
import character.Proc
import character.Stats
import character.Talent
import sim.SimIteration

class WeaponMastery(currentRank: Int) : Talent(currentRank) {
    companion object {
        const val name: String = "Weapon Mastery"
    }

    override val name: String = Companion.name
    override val maxRank: Int = 5

    override val buffs: List<Buff> = listOf(
        object : Buff() {
            override val name: String = Companion.name
            override val durationMs: Int = -1
            override val hidden: Boolean = true

            override fun modifyStats(sim: SimIteration, stats: Stats): Stats {
                val talentRanks = sim.subject.talents[WeaponMastery.name]?.currentRank ?: 0

                val modifier = 1 + (0.02 * talentRanks)
                return stats.add(
                    Stats(
                        whiteDamageMultiplier = modifier,
                        yellowDamageMultiplier = modifier
                    )
                )
            }

            override val procs: List<Proc> = listOf()
        }
    )

    override val procs: List<Proc> = listOf()
}
