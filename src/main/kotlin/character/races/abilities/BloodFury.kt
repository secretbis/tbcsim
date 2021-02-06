package character.races.abilities

import character.Ability
import character.Buff
import character.Stats
import sim.SimIteration

class BloodFury : Ability() {
    override val id: Int = 33697
    override val name: String = "Blood Fury"

    override fun cooldownMs(sim: SimIteration): Int = 120000
    // According to the internet, this was moved off of GCD in 3.0.3
    override fun gcdMs(sim: SimIteration): Int = sim.physicalGcd().toInt()

    val buff = object : Buff() {
        override val name: String = "Blood Fury"
        override val durationMs: Int = 15000

        override fun modifyStats(sim: SimIteration): Stats? {
            return Stats(
                attackPower = 6 + 4 * sim.subject.level,
                rangedAttackPower = 6 + 4 * sim.subject.level,
                spellDamage = 5 + 2 * sim.subject.level
            )
        }
    }

    override fun cast(sim: SimIteration) {
        sim.addBuff(buff)
    }
}
