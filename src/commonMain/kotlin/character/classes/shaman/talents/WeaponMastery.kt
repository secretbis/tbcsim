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

    val buff = object : Buff() {
        override val name: String = "Weapon Mastery"
        override val durationMs: Int = -1
        override val hidden: Boolean = true

        override fun modifyStats(sim: SimIteration): Stats {
            val talentRanks = sim.subject.klass.talents[WeaponMastery.name]?.currentRank ?: 0

            val modifier = 1 + (0.02 * talentRanks)
            return Stats(
                whiteDamageMultiplier = modifier,
                yellowDamageMultiplier = modifier
            )
        }
    }

    override fun buffs(sim: SimIteration): List<Buff> = listOf(buff)
}
