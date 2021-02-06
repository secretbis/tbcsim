package data.buffs.permanent

import character.Stats

class GenericFrostDamageBuff(val frostDamage: Int) : PermanentBuff() {
    override val name: String = "Frost Damage $frostDamage"

    override fun permanentStats(): Stats = Stats(frostDamage = frostDamage)
}
