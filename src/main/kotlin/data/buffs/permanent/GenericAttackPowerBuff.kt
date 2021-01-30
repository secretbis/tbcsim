package data.buffs.permanent

import character.Stats

class GenericAttackPowerBuff(val attackPower: Int) : PermanentBuff() {
    override val name: String = "Attack Power $attackPower"

    override fun permanentStats(): Stats = Stats(attackPower = attackPower, rangedAttackPower = attackPower)
}
