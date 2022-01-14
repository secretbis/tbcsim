package character.classes.warrior.abilities

import character.Stats
import sim.SimParticipant

class DefensiveStance: Stance() {
    companion object {
        const val name = "Defensive Stance"
    }

    override val id: Int = 71
    override val name: String = Companion.name
    override val icon: String = "ability_warrior_defensivestance.jpg"

    override fun cast(sp: SimParticipant) {
        sp.addBuff(stanceBuff(Companion.name, icon, Stats(
            physicalDamageMultiplier = 0.9,
            innateThreatMultiplier = 1.3
        )))
        super.cast(sp)
    }
}
