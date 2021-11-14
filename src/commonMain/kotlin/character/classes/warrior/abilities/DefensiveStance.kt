package character.classes.warrior.abilities

import character.Stats
import sim.SimParticipant

class DefensiveStance: Stance() {
    companion object {
        const val name = "Defensive Stance"
    }

    override val id: Int = 71
    override val name: String = Companion.name

    override fun cast(sp: SimParticipant) {
        sp.addBuff(stanceBuff(Companion.name, Stats(physicalDamageMultiplier = 0.9)))
        super.cast(sp)
    }
}
