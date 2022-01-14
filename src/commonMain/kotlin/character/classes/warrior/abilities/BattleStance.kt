package character.classes.warrior.abilities

import character.Stats
import sim.SimParticipant

class BattleStance: Stance() {
    companion object {
        const val name = "Battle Stance"
    }

    override val id: Int = 2457
    override val name: String = Companion.name
    override val icon: String = "ability_warrior_offensivestance.jpg"

    override fun cast(sp: SimParticipant) {
        sp.addBuff(stanceBuff(Companion.name, icon, Stats(innateThreatMultiplier = 0.8)))
        super.cast(sp)
    }
}
