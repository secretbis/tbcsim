package character.classes.warrior.talents

import character.Buff
import character.Stats
import character.Talent
import sim.SimIteration

class DualWieldSpec(currentRank: Int) : Talent(currentRank) {
    companion object {
        const val name = "Dual Wield Specialization"
    }

    override val name: String = Companion.name
    override val maxRank: Int = 5

    val buff = object : Buff() {
        override val name: String = "Dual Wield Specialization"
        override val durationMs: Int = -1
        override val hidden: Boolean = true

        override fun modifyStats(sim: SimIteration): Stats {
            val modifier = currentRank * 0.05
            return Stats(
                whiteDamageAddlOffHandPenaltyModifier = modifier,
                yellowDamageAddlOffHandPenaltyModifier = modifier
            )
        }
    }

    override fun buffs(sim: SimIteration): List<Buff> = listOf(buff)
}
