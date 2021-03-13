package data.abilities.raid

import character.Ability
import character.Buff
import character.Stats
import mechanics.Rating
import sim.SimParticipant

class TotemOfWrath : Ability() {
    companion object {
        const val name = "Totem of Wrath"
    }

    override val id: Int = 30706
    override val name: String = Companion.name
    override fun gcdMs(sp: SimParticipant): Int = 0

    val buff = object : Buff() {
        override val name: String = "Totem of Wrath"
        // Assume the caster is always maintaining this
        override val durationMs: Int = -1
        override val hidden: Boolean = true

        // Assume a GoA uptime of about 80% when twisting
        // Also assume the caster has Enhancing Totems
        override fun modifyStats(sp: SimParticipant): Stats? {
            return Stats(
                spellCritRating = 3.0 * Rating.critPerPct,
                spellHitRating = 3.0 * Rating.spellHitPerPct
            )
        }
    }

    override fun cast(sp: SimParticipant) {
        sp.addBuff(buff)
    }
}
