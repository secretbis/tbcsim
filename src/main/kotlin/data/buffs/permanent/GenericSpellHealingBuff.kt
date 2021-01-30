package data.buffs.permanent

import character.Stats

class GenericSpellHealingBuff(val spellHealing: Int) : PermanentBuff() {
    override val name: String = "Spell Healing $spellHealing"

    override fun permanentStats(): Stats = Stats(spellHealing = spellHealing)
}
