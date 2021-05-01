package character.classes.rogue.abilities

import character.Ability
import character.Buff
import character.classes.rogue.buffs.*
import sim.SimParticipant

class InstantPoisonMainHand : Ability() {
    companion object {
        const val name = "Instant Poison (MH)"
    }

    override val id: Int = 26890
    override val name: String = Companion.name
    override fun gcdMs(sp: SimParticipant): Int = sp.spellGcd().toInt()

    override fun available(sp: SimParticipant): Boolean {
        return sp.hasMainHandWeapon()
    }

    var buff: Buff? = null

    override fun cast(sp: SimParticipant) {
        if(buff == null) {
            buff = InstantPoison(sp.character.gear.mainHand)
        }
        sp.addBuff(buff!!)
    }
}
