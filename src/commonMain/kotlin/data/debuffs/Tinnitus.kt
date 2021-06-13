package data.debuffs

import character.Debuff
import sim.SimParticipant

class Tinnitus(owner: SimParticipant) : Debuff(owner) {
    companion object {
        const val name = "Tinnitus"
    }

    override val name: String = Companion.name
    override val durationMs: Int = 120000
}
