package character.classes.rogue.abilities

import character.*
import mechanics.Melee
import sim.Event
import sim.SimParticipant
import data.Constants
import data.model.Item
import character.classes.rogue.talents.*
import character.classes.rogue.debuffs.*
import character.classes.rogue.buffs.*
import mechanics.Rating
import mechanics.Spell

class Vanish : Ability() {
    companion object {
        const val name = "Vanish"
    }

    override val id: Int = 26889
    override val name: String = Companion.name

    override fun cooldownMs(sp: SimParticipant): Int {
        val elusiveness = sp.character.klass.talents[Elusiveness.name] as Elusiveness?
        val reducedCD = elusiveness?.cooldownReducedMs() ?: 0
        return 300000 - reducedCD
    }
    override fun gcdMs(sp: SimParticipant): Int = 0
    override val castableOnGcd: Boolean = true

    override fun resourceType(sp: SimParticipant): Resource.Type = Resource.Type.ENERGY
    override fun resourceCost(sp: SimParticipant): Double = 0.0

    override fun cast(sp: SimParticipant) {
        sp.addBuff(Stealth())
    }
}
