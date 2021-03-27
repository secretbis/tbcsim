package data.abilities.raid

import character.Ability
import character.Buff
import character.Stats
import sim.SimParticipant

class DrumsOfBattle : Ability() {
    companion object {
        const val name = "Drums of Battle"
    }

    override val id: Int = -1
    override val name: String = Companion.name
    override fun gcdMs(sp: SimParticipant): Int = 0

    val buff = object : Buff() {
        override val name: String = "Drums of Battle"
        override val durationMs: Int = -1
        override val hidden: Boolean = true

        override fun modifyStats(sp: SimParticipant): Stats {
            return Stats(
                physicalHasteRating = 80.0,
                spellHasteRating = 80.0
            )
        }
    }

    override fun cast(sp: SimParticipant) {
        sp.addBuff(buff)
    }
}
