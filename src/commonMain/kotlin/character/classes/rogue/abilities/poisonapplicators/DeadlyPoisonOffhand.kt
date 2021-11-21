package character.classes.rogue.abilities

import character.Ability
import character.Buff
import character.classes.rogue.buffs.*
import sim.SimParticipant

class DeadlyPoisonOffhand : Ability() {
    companion object {
        const val name = "Deadly Poison (OH)"
    }

    override val id: Int = 27186
    override val name: String = Companion.name
    override val icon: String = "ability_rogue_dualweild.jpg"
    override fun gcdMs(sp: SimParticipant): Int = sp.spellGcd().toInt()

    override fun available(sp: SimParticipant): Boolean {
        return sp.hasOffHandWeapon()
    }

    var buff: Buff? = null

    override fun cast(sp: SimParticipant) {
        if(buff == null) {
            buff = DeadlyPoison(sp.character.gear.offHand)
        }
        sp.addBuff(buff!!)
    }
}
