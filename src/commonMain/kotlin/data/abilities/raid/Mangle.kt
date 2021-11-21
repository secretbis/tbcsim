package data.abilities.raid

import character.*
import sim.SimParticipant

class Mangle : Ability() {
    companion object {
        const val name = "Mangle"
    }

    override val id: Int = 33987
    override val name: String = Companion.name
    override val icon: String = "ability_druid_mangle2.jpg"
    override fun gcdMs(sp: SimParticipant): Int = 0

    fun debuff(owner: SimParticipant) = object : Debuff(owner) {
        override val name: String = "Mangle"
        override val icon: String = "ability_druid_mangle2.jpg"
        // Assume the caster is always maintaining this
        override val durationMs: Int = -1
        override val hidden: Boolean = false
    }

    override fun cast(sp: SimParticipant) {
        sp.sim.target.addDebuff(debuff(sp))
    }
}
