package data.abilities.raid

import character.Ability
import character.Buff
import character.Stats
import sim.SimParticipant

class UnleashedRage : Ability() {
    companion object {
        const val name = "Unleashed Rage"
    }

    override val id: Int = 25359
    override val name: String = Companion.name
    override val icon: String = "spell_nature_unleashedrage.jpg"
    override fun gcdMs(sp: SimParticipant): Int = 0

    val buff = object : Buff() {
        override val name: String = "Unleashed Rage"
        override val icon: String = "spell_nature_unleashedrage.jpg"
        // Assume the caster is always maintaining this
        override val durationMs: Int = -1
        override val hidden: Boolean = true

        override fun modifyStats(sp: SimParticipant): Stats {
            return Stats(
                attackPowerMultiplier = 1.1,
                rangedAttackPowerMultiplier = 1.1
            )
        }
    }

    override fun cast(sp: SimParticipant) {
        sp.sim.addRaidBuff(buff)
    }
}
