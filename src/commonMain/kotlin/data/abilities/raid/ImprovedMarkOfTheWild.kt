package data.abilities.raid

import character.Ability
import character.Buff
import character.Stats
import sim.SimParticipant

class ImprovedMarkOfTheWild : Ability() {
    companion object {
        const val name = "Improved Mark of the Wild"
    }

    override val id: Int = 39233
    override val name: String = Companion.name
    override fun gcdMs(sp: SimParticipant): Int = 0

    // Always assume the raid buffer has 5/5 imp motw
    val multiplier = 1.35
    val armor = (340 * multiplier).toInt()
    val attr = (14 * multiplier).toInt()
    val resist = (25 * multiplier).toInt()

    val buff = object : Buff() {
        override val name: String = "Mark of the Wild"
        // Assume the caster is always maintaining this
        override val durationMs: Int = -1
        override val hidden: Boolean = true

        override fun modifyStats(sp: SimParticipant): Stats? {
            return Stats(
                armor = armor,
                strength = attr,
                agility = attr,
                intellect = attr,
                spirit = attr,
                stamina = attr,
                // FIXME: Implement proper resist stacking, if that ever becomes relevant
                fireResistance = resist,
                frostResistance = resist,
                natureResistance = resist,
                shadowResistance = resist,
                arcaneResistance = resist
            )
        }
    }

    override fun cast(sp: SimParticipant) {
        sp.addBuff(buff)
    }
}
