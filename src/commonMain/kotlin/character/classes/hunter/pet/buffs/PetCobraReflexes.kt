package character.classes.hunter.pet.buffs

import character.Buff
import character.Stats
import sim.SimParticipant

class PetCobraReflexes : Buff() {
    override val name: String = "Cobra Reflexes"
    override val durationMs: Int = -1
    override val hidden: Boolean = true

    override fun modifyStats(sp: SimParticipant): Stats {
        // Per rando wowhead comments, this is approximately a 15% damage reduction
        return Stats(
            physicalHasteMultiplier = 1.30,
            physicalDamageMultiplier = 0.85
        )
    }
}
