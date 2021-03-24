package data.buffs.permanent

import character.Stats

class GenericSpellHealingBuff(val spellHealing: Int) : PermanentBuff() {
    override val name: String = "Spell Healing $spellHealing"

    // Spell healing also increases spell damage by 1/3 of the amount
    override fun permanentStats(): Stats = Stats(
        spellHealing = spellHealing,
        spellDamage = (spellHealing / 3.0).toInt()
    )
}
