package data.abilities.raid

import character.Ability
import character.Buff
import character.Stats
import mechanics.Rating
import sim.SimParticipant

class Bloodlust : Ability() {
    companion object {
        const val name = "Bloodlust"
    }

    override val name: String = Companion.name
    override val icon: String = "spell_nature_bloodlust.jpg"

    val buff = object : Buff() {
        override val name: String = Companion.name
        override val icon: String = "spell_nature_bloodlust.jpg"
        override val durationMs: Int = 40000
        override val hidden: Boolean = false

        override fun modifyStats(sp: SimParticipant): Stats {
            return Stats(
                physicalHasteMultiplier = 1.3,
                spellHasteMultiplier = 1.3,
            )
        }
    }

    override fun cast(sp: SimParticipant) {
        sp.sim.addRaidBuff(buff)
    }
}
