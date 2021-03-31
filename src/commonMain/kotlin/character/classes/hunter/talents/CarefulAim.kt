package character.classes.hunter.talents

import character.Buff
import character.Stats
import character.Talent
import sim.SimParticipant

class CarefulAim(currentRank: Int) : Talent(currentRank) {
    companion object {
        const val name = "Careful Aim"
    }
    override val name: String = Companion.name
    override val maxRank: Int = 3

    val buff = object : Buff() {
        override val name: String = Companion.name
        override val durationMs: Int = -1
        override val hidden: Boolean = true

        override fun modifyStats(sp: SimParticipant): Stats {
            return Stats(
                rangedAttackPower = (sp.intellect() * 0.15 * currentRank).toInt()
            )
        }
    }

    override fun buffs(sp: SimParticipant): List<Buff> = listOf(buff)
}
