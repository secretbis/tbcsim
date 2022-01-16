package data.abilities.raid

import character.*
import sim.SimParticipant

class DemoralizingShout : Ability() {
    companion object {
        const val name = "Demoralizing Shout"
    }

    override val id: Int = 25203
    override val name: String = Companion.name
    override val icon: String = "ability_warrior_warcry.jpg"
    override fun gcdMs(sp: SimParticipant): Int = 0

    fun debuff(owner: SimParticipant) = object : Debuff(owner) {
        override val name: String = Companion.name
        override val icon: String = "ability_warrior_warcry.jpg"
        // Assume the caster is always maintaining this
        override val durationMs: Int = -1
        override val hidden: Boolean = false

        override fun modifyStats(sp: SimParticipant): Stats {
            return Stats(
                // Assume Imp Demo
                attackPower = -420
            )
        }
    }

    override fun cast(sp: SimParticipant) {
        sp.sim.target.addDebuff(debuff(sp))
    }
}
