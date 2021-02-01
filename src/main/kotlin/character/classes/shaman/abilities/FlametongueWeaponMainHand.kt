package character.classes.shaman.abilities

import character.Ability
import character.Buff
import character.classes.shaman.buffs.FlametongueWeapon
import sim.SimIteration

class FlametongueWeaponMainHand : Ability() {
    companion object {
        const val name = "Flametongue Weapon (MH)"
    }

    override val id: Int = 25489
    override val name: String = Companion.name
    override fun gcdMs(sim: SimIteration): Int = sim.spellGcd().toInt()

    override fun available(sim: SimIteration): Boolean {
        return sim.hasOffHandWeapon()
    }

    var buff: Buff? = null

    override fun cast(sim: SimIteration, free: Boolean) {
        if(buff == null) {
            buff = FlametongueWeapon(sim.subject.gear.mainHand)
        }
        sim.addBuff(buff!!)
    }
}
