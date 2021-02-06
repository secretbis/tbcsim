package data.buffs.permanent

import character.Stats

class GenericFeralAttackPowerBuff(val attackPower: Int) : PermanentBuff() {
    override val name: String = "Feral Attack Power $attackPower"

    override fun permanentStats(): Stats = Stats(feralAttackPower = attackPower)
}
