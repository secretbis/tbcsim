package data.buffs.permanent

import character.Stats

class GenericArcaneDamageBuff(val arcaneDamage: Int) : PermanentBuff() {
    override val name: String = "Arcane Damage $arcaneDamage"

    override fun permanentStats(): Stats = Stats(arcaneDamage = arcaneDamage)
}
