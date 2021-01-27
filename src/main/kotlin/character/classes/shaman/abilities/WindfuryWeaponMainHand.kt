package character.classes.shaman.abilities

import character.Ability
import character.Buff
import character.classes.shaman.buffs.WindfuryWeapon
import sim.SimIteration

class WindfuryWeaponMainHand : Ability() {
    companion object {
        const val name = "Windfury Weapon (MH)"
    }

    override val id: Int = 25505
    override val name: String = Companion.name
    override fun gcdMs(sim: SimIteration): Int = sim.subject.spellGcd().toInt()

    override fun available(sim: SimIteration): Boolean {
        return sim.subject.hasMainHandWeapon()
    }

    var buff: Buff? = null

    override fun cast(sim: SimIteration, free: Boolean) {
        if(buff == null) {
            buff = WindfuryWeapon(sim.subject.gear.mainHand)
        }
        sim.addBuff(buff!!)
    }

    override val baseCastTimeMs: Int = 0
}
