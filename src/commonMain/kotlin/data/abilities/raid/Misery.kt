package data.abilities.raid

import character.*
import sim.SimParticipant

class Misery : Ability() {
    companion object {
        const val name = "Misery"
    }

    override val id: Int = 33195
    override val name: String = Companion.name
    override val icon: String = "spell_shadow_misery.jpg"
    override fun gcdMs(sp: SimParticipant): Int = 0

    val buff = object : Buff() {
        override val name: String = "Misery"
        override val icon: String = "spell_shadow_misery.jpg"
        // Assume the caster is always maintaining this
        override val durationMs: Int = -1
        override val hidden: Boolean = true

        override fun modifyStats(sp: SimParticipant): Stats {
            return Stats(spellDamageMultiplier = 1.05)
        }
    }

    override fun cast(sp: SimParticipant) {
        sp.sim.addRaidBuff(buff)
    }
}
