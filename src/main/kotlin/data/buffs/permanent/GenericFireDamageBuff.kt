package data.buffs.permanent

import character.Stats

class GenericFireDamageBuff(val fireDamage: Int) : PermanentBuff() {
    override val name: String = "Fire Damage $fireDamage"

    override fun permanentStats(): Stats = Stats(fireDamage = fireDamage)
}
