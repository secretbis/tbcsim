package character.races.abilities

import character.Ability
import character.Buff
import character.Resource
import character.Stats
import sim.SimIteration

class Berserking : Ability() {
    override val id: Int = 26297
    override val name: String = "Berserking"

    override fun cooldownMs(sim: SimIteration): Int = 180000
    // According to the internet, this was moved off of GCD in 3.0.3
    override fun gcdMs(sim: SimIteration): Int = sim.physicalGcd().toInt()

    override fun resourceType(sim: SimIteration): Resource.Type {
        return sim.subject.klass.resourceType
    }

    override fun resourceCost(sim: SimIteration): Double {
        return when(sim.subject.klass.resourceType) {
            Resource.Type.MANA -> 0.06 * sim.subject.klass.baseMana
            Resource.Type.ENERGY -> 10.0
            Resource.Type.RAGE -> 5.0
        }
    }

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
