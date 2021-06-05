package character.classes.mage.abilities

import character.Ability
import character.Buff
import character.Stats
import sim.SimParticipant

class ArcaneIntellect : Ability() {
    companion object {
        const val name = "Arcane Intellect"
    }
    override val id: Int = 27126
    override val name: String = Companion.name
    override fun gcdMs(sp: SimParticipant): Int = sp.spellGcd().toInt()
    override fun resourceCost(sp: SimParticipant): Double = 700.0

    val buff = object : Buff() {
        override val name: String = Companion.name
        override val durationMs: Int = 30 * 60 * 1000

        override fun modifyStats(sp: SimParticipant): Stats {
            return Stats(intellect = 40)
        }
    }

    override fun cast(sp: SimParticipant) {
        sp.addBuff(buff)
    }
}
