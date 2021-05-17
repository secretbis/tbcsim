package character.classes.mage.abilities

import character.Ability
import character.Buff
import character.Stats
import mechanics.Rating
import sim.SimParticipant

class MoltenArmor : Ability() {
    companion object {
        const val name = "Molten Armor"
    }
    override val id: Int = 30482
    override val name: String = Companion.name
    override fun gcdMs(sp: SimParticipant): Int = sp.spellGcd().toInt()
    override fun resourceCost(sp: SimParticipant): Double = 630.0

    val buff = object : Buff() {
        override val name: String = Companion.name
        override val durationMs: Int = 30 * 60 * 1000

        override fun modifyStats(sp: SimParticipant): Stats {
            return Stats(spellCritRating = 3.0 * Rating.critPerPct)
        }
    }

    override fun cast(sp: SimParticipant) {
        sp.addBuff(buff)
    }
}
