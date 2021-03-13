package data.abilities.raid

import character.*
import sim.SimParticipant

class SunderArmor : Ability() {
    companion object {
        const val name = "Sunder Armor"
    }

    override val id: Int = 25225
    override val name: String = Companion.name
    override fun gcdMs(sp: SimParticipant): Int = 0

    val debuff = object : Debuff() {
        override val name: String = "Sunder Armor"
        // Assume the caster is always maintaining this
        override val durationMs: Int = -1
        override val hidden: Boolean = true

        override fun modifyStats(sp: SimParticipant): Stats? {
            val impEaActive = sp.debuffs[ImprovedExposeArmor.name] != null
            return if(impEaActive) {
                null
            } else {
                Stats(
                    armor = -1 * 520 * 5
                )
            }
        }
    }

    override fun cast(sp: SimParticipant) {
        sp.addDebuff(debuff)
    }
}
