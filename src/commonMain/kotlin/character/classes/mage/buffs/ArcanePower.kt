package character.classes.mage.buffs

import character.Buff
import character.Stats
import sim.SimParticipant

class ArcanePower : Buff() {
    companion object {
        const val name = "Arcane Power"
    }

    override val name: String = Companion.name
    override val durationMs: Int = 15000
    override val icon: String = "spell_nature_lightning.jpg"

    fun manaCostMultiplier(): Double = 1.3

    override fun modifyStats(sp: SimParticipant): Stats {
        return Stats(spellDamageMultiplier = 1.3)
    }
}
