package character.classes.shaman.abilities

import character.Ability
import character.Buff
import character.classes.shaman.buffs.FlametongueWeapon
import sim.SimParticipant

class FlametongueWeaponMainHand : Ability() {
    companion object {
        const val name = "Flametongue Weapon (MH)"
    }

    override val id: Int = 25489
    override val name: String = Companion.name
    override val icon: String = "spell_fire_flametounge.jpg"
    override fun gcdMs(sp: SimParticipant): Int = sp.spellGcd().toInt()

    override fun available(sp: SimParticipant): Boolean {
        return sp.hasOffHandWeapon()
    }

    var buff: Buff? = null

    override fun cast(sp: SimParticipant) {
        if(buff == null) {
            buff = FlametongueWeapon(sp.character.gear.mainHand)
        }
        sp.addBuff(buff!!)
    }
}
