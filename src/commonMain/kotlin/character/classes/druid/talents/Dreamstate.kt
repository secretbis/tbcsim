package character.classes.druid.talents

import character.Buff
import character.Stats
import character.Talent
import sim.SimParticipant

/**
 *
 */
class Dreamstate(currentRank: Int) : Talent(currentRank) {
    companion object {
        const val name = "Dreamstate"
    }

    override val name: String = Companion.name
    override val maxRank: Int = 3

    val buff = object : Buff() {
        override val name: String = Companion.name
        override val durationMs: Int = -1
        override val hidden: Boolean = true

        override fun modifyStats(sp: SimParticipant): Stats {
            val percentOfInt: Double = when(currentRank) {
                3 -> .1
                2 -> .07
                1 -> .04
                else -> 0.0
            }

            return Stats(manaPer5Seconds = (sp.intellect() * percentOfInt).toInt())
        }
    }

    override fun buffs(sp: SimParticipant): List<Buff> = listOf(buff)

}