package character.classes.warrior.abilities

import character.Stats
import mechanics.Rating
import sim.SimParticipant

class BerserkerStance: Stance() {
    companion object {
        const val name = "Berserker Stance"
    }

    override val id: Int = 2458
    override val name: String = Companion.name
    override val icon: String = "ability_racial_avatar.jpg"

    override fun cast(sp: SimParticipant) {
        sp.addBuff(stanceBuff(Companion.name, icon, Stats(meleeCritRating = 3.0 * Rating.critPerPct)))
        super.cast(sp)
    }
}
