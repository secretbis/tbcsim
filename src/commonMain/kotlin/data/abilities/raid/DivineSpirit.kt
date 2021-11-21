package data.abilities.raid

import character.Ability
import character.Buff
import character.Stats
import mechanics.Rating
import sim.SimParticipant

class DivineSpirit : Ability() {
    companion object {
        const val name = "Divine Spirit"
    }

    override val id: Int = 25312
    override val name: String = Companion.name
    override val icon: String = "spell_holy_prayerofspirit.jpg"
    override fun gcdMs(sp: SimParticipant): Int = 0

    val buff = object : Buff() {
        override val name: String = Companion.name
        override val icon: String = "spell_holy_prayerofspirit.jpg"
        override val durationMs: Int = -1

        override fun modifyStats(sp: SimParticipant): Stats {
            return Stats(spirit = 50)
        }
    }

    override fun cast(sp: SimParticipant) {
        sp.sim.addRaidBuff(buff)
    }
}
