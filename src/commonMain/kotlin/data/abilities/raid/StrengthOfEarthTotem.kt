package data.abilities.raid

import character.Ability
import character.Buff
import character.Stats
import sim.SimParticipant

class StrengthOfEarthTotem : Ability() {
    companion object {
        const val name = "Strength of Earth Totem"
    }

    override val id: Int = 25528
    override val name: String = Companion.name
    override val icon: String = "spell_nature_earthbindtotem.jpg"
    override fun gcdMs(sp: SimParticipant): Int = 0

    val buff = object : Buff() {
        override val name: String = "Strength of Earth Totem"
        override val icon: String = "spell_nature_earthbindtotem.jpg"
        // Assume the caster is always maintaining this
        override val durationMs: Int = -1
        override val hidden: Boolean = true

        // Assume 100% uptime and that the caster has Enhancing Totems
        override fun modifyStats(sp: SimParticipant): Stats {
            return Stats(
                strength = (86.0 * 1.15).toInt()
            )
        }
    }

    override fun cast(sp: SimParticipant) {
        sp.sim.addRaidBuff(buff)
    }
}
