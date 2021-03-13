package character.races.abilities

import character.Ability
import character.Buff
import character.Stats
import sim.SimParticipant

class BloodFury : Ability() {
    override val id: Int = 33697
    override val name: String = "Blood Fury"

    override fun cooldownMs(sp: SimParticipant): Int = 120000
    // According to the internet, this was moved off of GCD in 3.0.3
    override fun gcdMs(sp: SimParticipant): Int = sp.physicalGcd().toInt()

    val buff = object : Buff() {
        override val name: String = "Blood Fury"
        override val durationMs: Int = 15000

        override fun modifyStats(sp: SimParticipant): Stats? {
            return Stats(
                attackPower = 6 + 4 * sp.character.level,
                rangedAttackPower = 6 + 4 * sp.character.level,
                spellDamage = 5 + 2 * sp.character.level
            )
        }
    }

    override fun cast(sp: SimParticipant) {
        sp.addBuff(buff)
    }
}
