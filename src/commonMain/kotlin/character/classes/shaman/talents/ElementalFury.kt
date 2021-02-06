package character.classes.shaman.talents

import character.Buff
import character.Stats
import character.Talent
import sim.SimIteration

class ElementalFury(currentRank: Int) : Talent(currentRank) {
    companion object {
        const val name = "Elemental Fury"
    }

    override val name: String = Companion.name
    override val maxRank: Int = 1

    val buff = object : Buff() {
        override val name: String = Companion.name
        override val durationMs: Int = -1
        override val hidden: Boolean = true

        override fun modifyStats(sim: SimIteration): Stats {
            return Stats(spellDamageAddlCritMultiplier = 1.5)
        }
    }

    override fun buffs(sim: SimIteration): List<Buff> = listOf(buff)
}
