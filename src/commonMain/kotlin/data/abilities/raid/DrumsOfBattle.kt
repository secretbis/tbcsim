package data.abilities.raid

import character.Ability
import character.Buff
import character.Debuff
import character.Stats
import data.debuffs.Tinnitus
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
            // With Tinnitus, this only gets 25% uptime
            return Stats(
                physicalHasteRating = 20.0,
                spellHasteRating = 20.0
            )
        }
    }

    override fun cast(sp: SimParticipant) {
        sp.sim.addRaidBuff(buff)
    }
}
