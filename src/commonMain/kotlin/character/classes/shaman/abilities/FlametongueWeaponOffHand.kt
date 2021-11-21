package character.classes.shaman.abilities

import character.Ability
import character.Buff
import character.classes.shaman.buffs.FlametongueWeapon
import data.itemscustom.EmptyItem
import sim.SimParticipant

class FlametongueWeaponOffHand : Ability() {
    companion object {
        const val name = "Flametongue Weapon (OH)"
    }

    override val id: Int = 25489
    override val name: String = Companion.name
    override val icon: String = "spell_fire_flametounge.jpg"
    override fun gcdMs(sp: SimParticipant): Int = sp.spellGcd().toInt()

    override fun available(sp: SimParticipant): Boolean {
        return true
    }

    var buff: Buff? = null

    override fun cast(sp: SimParticipant) {
        if(sp.character.gear.offHand !is EmptyItem) {
            if (buff == null) {
                buff = FlametongueWeapon(sp.character.gear.offHand)
            }
            sp.addBuff(buff!!)
        }
    }
}
