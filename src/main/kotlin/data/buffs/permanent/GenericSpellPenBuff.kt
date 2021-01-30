package data.buffs.permanent

import character.Stats

class GenericSpellPenBuff(val spellPen: Int) : PermanentBuff() {
    override val name: String = "Spell Penetration $spellPen"

    override fun permanentStats(): Stats = Stats(spellPen = spellPen)
}
