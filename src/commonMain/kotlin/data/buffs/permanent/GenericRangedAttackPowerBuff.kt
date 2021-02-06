package data.buffs.permanent

import character.Stats

class GenericRangedAttackPowerBuff(val rangedAttackPower: Int) : PermanentBuff() {
    override val name: String = "Ranged Attack Power $rangedAttackPower"

    override fun permanentStats(): Stats = Stats(rangedAttackPower = rangedAttackPower)
}
