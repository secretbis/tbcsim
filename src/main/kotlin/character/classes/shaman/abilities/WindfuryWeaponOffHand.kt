package character.classes.shaman.abilities

import character.Ability
import character.Buff
import character.classes.shaman.buffs.WindfuryWeapon
import sim.SimIteration

class WindfuryWeaponOffHand : Ability() {
    companion object {
        const val name = "Windfury Weapon (OH)"
    }

    override val id: Int = 25505
    override val name: String = Companion.name
    override fun gcdMs(sim: SimIteration): Int = sim.spellGcd().toInt()

    override fun available(sim: SimIteration): Boolean {
        return sim.hasOffHandWeapon()
    }

    var buff: Buff? = null

    override fun cast(sim: SimIteration) {
        if(buff == null) {
            buff = WindfuryWeapon(sim.subject.gear.offHand)
        }
        sim.addBuff(buff!!)
    }
}
