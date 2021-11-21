package character.classes.rogue.debuffs

import character.Ability
import character.Debuff
import data.Constants
import sim.Event
import sim.SimParticipant
import character.Proc
import character.classes.rogue.talents.*

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
