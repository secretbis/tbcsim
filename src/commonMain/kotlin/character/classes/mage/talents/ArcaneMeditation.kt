package character.classes.mage.talents

import character.Buff
import character.Stats
import character.Talent
import mechanics.General
import sim.SimParticipant

class ArcaneMeditation(currentRank: Int) : Talent(currentRank) {
    companion object {
        const val name = "Arcane Meditation"
    }
    override val name: String = Companion.name
    override val maxRank: Int = 3

    val buff = object : Buff() {
        override val name: String = Companion.name
        override val durationMs: Int = -1
        override val hidden: Boolean = true

        override fun modifyStats(sp: SimParticipant): Stats {
            return Stats(manaPer5Seconds = (General.mp5FromSpiritNotCasting(sp) * 0.1 * currentRank).toInt())
        }
    }

    override fun buffs(sp: SimParticipant): List<Buff> = listOf(buff)
}
