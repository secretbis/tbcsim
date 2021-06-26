package data.abilities.raid

import character.Ability
import character.Buff
import character.Stats
import sim.SimParticipant

class ImprovedDivineSpirit : Ability() {
    companion object {
        const val name = "Improved Divine Spirit"
    }

    override val id: Int = 25312
    override val name: String = Companion.name
    override fun gcdMs(sp: SimParticipant): Int = 0

    val buff = object : Buff() {
        override val name: String = "Improved Divine Spirit"
        // Assume the caster is always maintaining this
        override val durationMs: Int = -1
        override val hidden: Boolean = true

        override fun modifyStats(sp: SimParticipant): Stats {
            return Stats(
                spirit = 50,
                spellDamage = ((sp.spirit() + 50) * 0.1).toInt() 
            )
        }
    }

    override fun cast(sp: SimParticipant) {
        sp.sim.addRaidBuff(buff)
    }
}
