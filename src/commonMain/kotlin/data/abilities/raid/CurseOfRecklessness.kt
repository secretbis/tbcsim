package data.abilities.raid

import character.*
import sim.SimParticipant

class CurseOfRecklessness : Ability() {
    companion object {
        const val name = "Curse of Recklessness"
    }

    override val id: Int = 27226
    override val name: String = Companion.name
    override val icon: String = "spell_shadow_unholystrength.jpg"
    override fun gcdMs(sp: SimParticipant): Int = 0

    fun debuff(owner: SimParticipant) = object : Debuff(owner) {
         override val name: String = "Curse of Recklessness"
        override val icon: String = "spell_shadow_unholystrength.jpg"
        // Assume the caster is always maintaining this
        override val durationMs: Int = -1
        override val hidden: Boolean = true

        override fun modifyStats(sp: SimParticipant): Stats {
            return Stats(
                armor = -800,
                attackPower = 135,
                rangedAttackPower = 135
            )
        }
    }

    override fun cast(sp: SimParticipant) {
        sp.sim.target.addDebuff(debuff(sp))
    }
}
