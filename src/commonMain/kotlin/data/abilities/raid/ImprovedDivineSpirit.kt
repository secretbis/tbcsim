package data.abilities.raid

import character.Ability
import character.Buff
import character.Stats
import mechanics.Rating
import sim.SimParticipant

class ImprovedDivineSpirit : Ability() {
    companion object {
        const val name = "Improved Divine Spirit"
    }

    override val id: Int = 33182
    override val name: String = Companion.name
    override fun gcdMs(sp: SimParticipant): Int = 0

    val buff = object : Buff() {
        override val name: String = Companion.name
        override val durationMs: Int = -1

        override fun modifyStats(sp: SimParticipant): Stats {
            // assumes max rank
            val increase = (0.1 * sp.stats.spirit).toInt();

            return Stats(
                spellDamage = increase,
                spellHealing = increase,
            )
        }
    }

    override fun cast(sp: SimParticipant) {
        // Ensure Divine Spirit buff is present as it's not
        // possible to have Improved Divine Spirit without it
        sp.sim.addRaidBuff(DivineSpirit().buff)
        sp.sim.addRaidBuff(buff)
    }
}
