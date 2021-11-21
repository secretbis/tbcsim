package character.classes.rogue.abilities

import character.Ability
import character.Buff
import character.classes.rogue.buffs.*
import sim.SimParticipant

class WoundPoisonMainhand : Ability() {
    companion object {
        const val name = "Wound Poison (MH)"
    }

    override val id: Int = 27189
    override val name: String = Companion.name
    override val icon: String = "inv_misc_herb_16.jpg"
    override fun gcdMs(sp: SimParticipant): Int = sp.spellGcd().toInt()

    override fun available(sp: SimParticipant): Boolean {
        return sp.hasMainHandWeapon()
    }

    var buff: Buff? = null

    override fun cast(sp: SimParticipant) {
        if(buff == null) {
            buff = WoundPoison(sp.character.gear.mainHand)
        }
        sp.addBuff(buff!!)
    }
}
