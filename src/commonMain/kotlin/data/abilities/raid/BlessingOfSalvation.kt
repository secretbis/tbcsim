package data.abilities.raid

import character.Ability
import character.Buff
import character.Stats
import sim.SimParticipant

class BlessingOfSalvation : Ability() {
    companion object {
        const val name = "Blessing of Salvation"
    }

    override val id: Int = 25895
    override val name: String = Companion.name
    override val icon: String = "spell_holy_greaterblessingofsalvation.jpg"
    override fun gcdMs(sp: SimParticipant): Int = 0

    val buff = object : Buff() {
        override val name: String = Companion.name
        override val icon: String = "spell_holy_greaterblessingofsalvation.jpg"
        // Assume the caster is always maintaining this
        override val durationMs: Int = -1
        override val hidden: Boolean = true

        override fun modifyStats(sp: SimParticipant): Stats {
            return Stats(
                innateThreatMultiplier = 0.7
            )
        }
    }

    override fun cast(sp: SimParticipant) {
        sp.sim.addRaidBuff(buff)
    }
}
