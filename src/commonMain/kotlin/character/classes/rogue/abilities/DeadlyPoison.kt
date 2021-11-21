package character.classes.rogue.abilities

import character.Ability
import character.Proc
import data.Constants
import data.model.Item
import mechanics.Melee
import sim.Event
import sim.SimParticipant

class DeadlyPoison(override val name: String, val item: Item) : Ability() {
    companion object {
        const val name = "Deadly Poison"
    }

    override val id: Int = 27187
    override val icon: String = "ability_rogue_dualweild.jpg"
    override fun gcdMs(sp: SimParticipant): Int = 0
    override val castableOnGcd: Boolean = true

    override fun available(sp: SimParticipant): Boolean {
        return if(Melee.isOffhand(sp, item)) { sp.isDualWielding() } else true
    }

    override fun cast(sp: SimParticipant) {
        sp.sim.target.addDebuff(character.classes.rogue.debuffs.DeadlyPoisonDot(sp))
    }
}
