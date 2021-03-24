package character.races.abilities

import character.Ability
import character.Buff
import character.Resource
import character.Stats
import sim.SimParticipant

class Berserking : Ability() {
    override val id: Int = 26297
    override val name: String = "Berserking"

    override fun cooldownMs(sp: SimParticipant): Int = 180000
    // According to the internet, this was moved off of GCD in 3.0.3
    override fun gcdMs(sp: SimParticipant): Int = sp.physicalGcd().toInt()

    override fun resourceType(sp: SimParticipant): Resource.Type {
        return sp.character.klass.resourceType
    }

    override fun resourceCost(sp: SimParticipant): Double {
        return when(sp.character.klass.resourceType) {
            Resource.Type.MANA -> 0.06 * sp.character.klass.baseMana
            Resource.Type.ENERGY -> 10.0
            Resource.Type.RAGE -> 5.0
            else -> 0.0
        }
    }

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
