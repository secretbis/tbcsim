package character.classes.rogue.abilities

import character.*
import character.classes.rogue.buffs.*
import character.classes.rogue.debuffs.*
import character.classes.rogue.talents.*
import sim.SimParticipant

class Vanish : Ability() {
    companion object {
        const val name = "Vanish"
    }

    override val id: Int = 26889
    override val name: String = Companion.name
    override val icon: String = "ability_vanish.jpg"

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
