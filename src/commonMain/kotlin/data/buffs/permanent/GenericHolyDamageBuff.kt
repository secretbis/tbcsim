package data.buffs.permanent

import character.Stats

class GenericHolyDamageBuff(val holyDamage: Int) : PermanentBuff() {
    override val name: String = "Holy Damage $holyDamage"

    override fun permanentStats(): Stats = Stats(holyDamage = holyDamage)
}
