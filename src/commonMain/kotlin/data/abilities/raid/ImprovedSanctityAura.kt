package data.abilities.raid

import character.Ability
import character.Buff
import character.Stats
import sim.SimParticipant

class ImprovedSanctityAura : Ability() {
    companion object {
        const val name = "Improved Sanctity Aura"
    }

    override val id: Int = 31870
    override val name: String = Companion.name
    override val icon: String = "spell_holy_mindvision.jpg"
    override fun gcdMs(sp: SimParticipant): Int = 0

    val buff = object : Buff() {
        override val name: String = "Sanctity Aura"
        override val icon: String = "spell_holy_mindvision.jpg"
        // Assume the caster is always maintaining this
        override val durationMs: Int = -1
        override val hidden: Boolean = true

        override fun modifyStats(sp: SimParticipant): Stats {
            return Stats(
                physicalDamageMultiplier = 1.02
            )
        }
    }

    override fun cast(sp: SimParticipant) {
        sp.sim.addRaidBuff(buff)
    }
}
