package character.classes.mage.talents

import character.Buff
import character.Stats
import character.Talent
import mechanics.Rating
import sim.SimParticipant

class SpellPower(currentRank: Int) : Talent(currentRank) {
    companion object {
        const val name = "Spell Power"
    }
    override val name: String = Companion.name
    override val maxRank: Int = 2

    val buff = object : Buff() {
        override val name: String = Companion.name
        override val durationMs: Int = -1
        override val hidden: Boolean = true
        override val icon: String = "spell_arcane_arcanetorrent.jpg"

        override fun modifyStats(sp: SimParticipant): Stats {
            return Stats(spellDamageAddlCritMultiplier = 1.0 + (currentRank * 0.25))
        }
    }

    override fun buffs(sp: SimParticipant): List<Buff> = listOf(buff)
}
