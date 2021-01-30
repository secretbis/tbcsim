package data.buffs.permanent

import character.Stats

class GenericNatureDamageBuff(val natureDamage: Int) : PermanentBuff() {
    override val name: String = "Nature Damage"

    override fun permanentStats(): Stats = Stats(natureDamage = natureDamage)
}
