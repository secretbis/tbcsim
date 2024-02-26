package character.classes.druid.talents

import character.Buff
import character.Stats
import character.Talent
import mechanics.General
import sim.SimParticipant

/**
 *
 */
class Intensity(currentRank: Int) : Talent(currentRank) {
    companion object {
        const val name = "Intensity"
    }

    override val name: String = Companion.name
    override val maxRank: Int = 3

    val buff = object : Buff() {
        override val name: String = Companion.name
        override val durationMs: Int = -1
        override val hidden: Boolean = true

        override fun modifyStats(sp: SimParticipant): Stats {
            val percentOfRegen: Double = currentRank * .1

            return Stats(manaPer5Seconds = (General.mp5FromSpiritNotCasting(sp) * percentOfRegen).toInt())
        }
    }

    override fun buffs(sp: SimParticipant): List<Buff> = listOf(buff)

}