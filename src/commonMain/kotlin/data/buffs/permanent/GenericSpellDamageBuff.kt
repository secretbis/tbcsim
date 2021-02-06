package data.buffs.permanent

import character.Stats

class GenericSpellDamageBuff(val spellDamage: Int) : PermanentBuff() {
    override val name: String = "Spell Damage $spellDamage"

    override fun permanentStats(): Stats = Stats(spellDamage = spellDamage)
}
