package character.classes.shaman.buffs

import character.Buff
import character.Proc
import character.Stats
import character.classes.shaman.talents.UnleashedRage as UnleashedRageTalent
import sim.Sim
import kotlin.math.floor

class UnleashedRage : Buff() {
    override var appliedAtMs: Int = 0
    override val durationMs: Int = 10000
    override val statModType: ModType = ModType.PERCENTAGE
    override val hidden: Boolean = false

    override fun modifyStats(sim: Sim, stats: Stats): Stats {
        val talentRanks = sim.subject.klass.talents.find {
            it.name == UnleashedRageTalent.name
        }?.currentRank ?: 0

        val modifier = 1 * (0.2 * talentRanks)
        stats.attackPower = floor(stats.attackPower * modifier).toInt()
        stats.rangedAttackPower = floor(stats.rangedAttackPower * modifier).toInt()
        return stats
    }

    override val procs: List<Proc> = listOf()
}
