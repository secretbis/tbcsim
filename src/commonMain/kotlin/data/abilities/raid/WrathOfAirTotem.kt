package data.abilities.raid

import character.Ability
import character.Buff
import character.Stats
import sim.SimParticipant

class WrathOfAirTotem : Ability() {
    companion object {
        const val name = "Wrath of Air Totem"
    }

    override val id: Int = 30706
    override val name: String = Companion.name
    override val icon: String = "spell_nature_slowingtotem.jpg"
    override fun gcdMs(sp: SimParticipant): Int = 0

    val buff = object : Buff() {
        override val name: String = Companion.name
        override val icon: String = "spell_nature_slowingtotem.jpg"
        // Assume the caster is always maintaining this
        override val durationMs: Int = -1
        override val hidden: Boolean = true

        override fun modifyStats(sp: SimParticipant): Stats {
            return Stats(
                spellDamage = 101
            )
        }
    }

    override fun cast(sp: SimParticipant) {
        sp.sim.addRaidBuff(buff)
    }
}
