package character.classes.hunter.pet.buffs

import character.Buff
import character.Stats
import sim.SimParticipant

class PetHappiness : Buff() {
    override val name: String = "Pet Happiness"
    override val durationMs: Int = -1
    override val hidden: Boolean = true

    // Let's assume everyone keeps their good bois well fed
    override fun modifyStats(sp: SimParticipant): Stats {
        val happinessMultiplier = 1.25
        return Stats(
            physicalDamageMultiplier = happinessMultiplier,
            spellDamageMultiplier = happinessMultiplier
        )
    }
}
