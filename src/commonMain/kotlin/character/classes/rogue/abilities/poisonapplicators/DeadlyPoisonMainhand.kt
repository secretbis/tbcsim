package character.classes.rogue.abilities

import character.Ability
import character.Buff
import character.classes.rogue.buffs.*
import sim.SimParticipant

class DeadlyPoisonMainHand : Ability() {
    companion object {
        const val name = "Deadly Poison (MH)"
    }

    override val id: Int = 27186
    override val name: String = Companion.name
    override fun gcdMs(sp: SimParticipant): Int = sp.spellGcd().toInt()

    override fun available(sp: SimParticipant): Boolean {
        return sp.hasMainHandWeapon()
    }

    var buff: Buff? = null

    // not adding the healing debuff since it's irrelevant for sims
    override fun cast(sp: SimParticipant) {
        if(buff == null) {
            buff = DeadlyPoison(sp.character.gear.mainHand)
        }
        sp.addBuff(buff!!)
    }
}
