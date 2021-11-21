package data.abilities.raid

import character.Ability
import character.Buff
import character.Stats
import sim.SimParticipant

class BlessingOfKings : Ability() {
    companion object {
        const val name = "Blessing of Kings"
    }

    override val id: Int = 20217
    override val name: String = Companion.name
    override val icon: String = "spell_magic_greaterblessingofkings.jpg"
    override fun gcdMs(sp: SimParticipant): Int = 0

    val buff = object : Buff() {
        override val name: String = "Blessing of Kings"
        override val icon: String = "spell_magic_greaterblessingofkings.jpg"
        // Assume the caster is always maintaining this
        override val durationMs: Int = -1
        override val hidden: Boolean = true

        override fun modifyStats(sp: SimParticipant): Stats {
            return Stats(
                strengthMultiplier = 1.1,
                agilityMultiplier = 1.1,
                intellectMultiplier = 1.1,
                spiritMultiplier = 1.1,
                staminaMultiplier = 1.1
            )
        }
    }

    override fun cast(sp: SimParticipant) {
        sp.sim.addRaidBuff(buff)
    }
}
