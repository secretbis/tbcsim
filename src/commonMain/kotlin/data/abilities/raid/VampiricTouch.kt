package data.abilities.raid

import character.Ability
import character.Buff
import character.Stats
import sim.SimParticipant

class VampiricTouch(val dps: Int): Ability() {
    override val id: Int = 34917
    override val name: String = "Vampiric Touch ($dps DPS)"
    override val icon: String = "spell_holy_stoicism.jpg"
    override fun gcdMs(sp: SimParticipant): Int = 0

    val buff = object : Buff() {
        override val name: String = "Vampiric Touch ($dps DPS)"
        override val icon: String = "spell_holy_stoicism.jpg"
        // Assume the caster is always maintaining this
        override val durationMs: Int = -1
        override val hidden: Boolean = true

        override fun modifyStats(sp: SimParticipant): Stats {
            return Stats(
                manaPer5Seconds = (0.05 * dps * 5.0).toInt()
            )
        }
    }

    override fun cast(sp: SimParticipant) {
        sp.sim.addRaidBuff(buff)
    }
}
