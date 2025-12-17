package character.classes.rogue.debuffs

import character.Debuff
import character.classes.rogue.talents.*
import sim.SimParticipant

// this is just an empty buff to be able to apply the mutilate bonus damage

class WoundPoison(owner: SimParticipant) : Debuff(owner) {
    companion object {
        const val name = "Wound Poison"
    }

    override val name: String = Companion.name
    override val icon: String = "inv_misc_herb_16.jpg"
    override val durationMs: Int = 15000
    override val maxStacks: Int = 5
}
