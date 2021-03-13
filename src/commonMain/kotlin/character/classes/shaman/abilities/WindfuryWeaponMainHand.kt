package character.classes.shaman.abilities

import character.Ability
import character.Buff
import character.classes.shaman.buffs.WindfuryWeapon
import sim.SimParticipant

class WindfuryWeaponMainHand : Ability() {
    companion object {
        const val name = "Windfury Weapon (MH)"
    }

    override val id: Int = 25505
    override val name: String = Companion.name
    override fun gcdMs(sp: SimParticipant): Int = sp.spellGcd().toInt()

    override fun available(sp: SimParticipant): Boolean {
        return sp.hasMainHandWeapon()
    }

    var buff: Buff? = null

    override fun cast(sp: SimParticipant) {
        if(buff == null) {
            buff = WindfuryWeapon(sp.character.gear.mainHand)
        }
        sp.addBuff(buff!!)
    }
}
