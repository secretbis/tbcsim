package data.abilities.raid

import character.*
import sim.SimParticipant

class ThunderClap : Ability() {
    companion object {
        const val name = "Thunder Clap"
    }

    override val id: Int = 25264
    override val name: String = Companion.name
    override val icon: String = "spell_nature_thunderclap.jpg"
    override fun gcdMs(sp: SimParticipant): Int = 0

    fun debuff(owner: SimParticipant) = object : Debuff(owner) {
        override val name: String = Companion.name
        override val icon: String = "spell_nature_thunderclap.jpg"
        // Assume the caster is always maintaining this
        override val durationMs: Int = -1
        override val hidden: Boolean = false

        override fun modifyStats(sp: SimParticipant): Stats {
            return Stats(
                // Assume Imp Thunder Clap
                physicalHasteMultiplier = 0.8
            )
        }
    }

    override fun cast(sp: SimParticipant) {
        sp.sim.target.addDebuff(debuff(sp))
    }
}
