package character.auto

import data.model.Item
import sim.SimParticipant
import character.classes.rogue.buffs.*

class MeleeMainHandRogue : AutoAttackBase() {
    companion object {
        const val name = "Melee (MH)"
    }

    override val id: Int = 1
    override val name: String = Companion.name

    override fun item(sp: SimParticipant): Item = sp.character.gear.mainHand

    override fun available(sp: SimParticipant): Boolean {
        val stealthBuff = sp.buffs[Stealth.name]
        val inStealth = stealthBuff != null
        return !inStealth && sp.hasMainHandWeapon() && super.available(sp)
    }
}
