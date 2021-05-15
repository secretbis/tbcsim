package character.classes.mage.talents

import character.Buff
import character.Stats
import character.Talent
import sim.SimParticipant

class MoltenFury(currentRank: Int) : Talent(currentRank) {
    companion object {
        const val name = "Molten Fury"
    }
    override val name: String = Companion.name
    override val maxRank: Int = 2

    val buff = object : Buff() {
        override val name: String = Companion.name
        override val durationMs: Int = -1
        override val hidden: Boolean = true

        override fun modifyStats(sp: SimParticipant): Stats {
            return if(sp.sim.isExecutePhase()) {
                Stats(spellDamageMultiplier = 1.0 + (currentRank * 0.1))
            } else Stats()
        }
    }

    override fun buffs(sp: SimParticipant): List<Buff> = listOf(buff)
}
