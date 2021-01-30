package data.buffs.permanent

import character.Stats

class GenericShadowDamageBuff(val shadowDamage: Int) : PermanentBuff() {
    override val name: String = "Shadow Damage $shadowDamage"

    override fun permanentStats(): Stats = Stats(shadowDamage = shadowDamage)
}
