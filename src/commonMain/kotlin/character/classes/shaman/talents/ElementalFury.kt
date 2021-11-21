package character.classes.shaman.talents

import character.Buff
import character.Stats
import character.Talent
import sim.SimParticipant

class ElementalFury(currentRank: Int) : Talent(currentRank) {
    companion object {
        const val name = "Elemental Fury"
    }

    override val name: String = Companion.name
    override val maxRank: Int = 1

    val buff = object : Buff() {
        override val name: String = "Elemental Fury"
        override val icon: String = "spell_fire_volcano.jpg"
        override val durationMs: Int = -1
        override val hidden: Boolean = true

        override fun modifyStats(sp: SimParticipant): Stats {
            return Stats(spellDamageAddlCritMultiplier = 2.0)
        }
    }

    override fun buffs(sp: SimParticipant): List<Buff> = listOf(buff)
}
