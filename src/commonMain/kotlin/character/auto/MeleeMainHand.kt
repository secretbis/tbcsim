package character.auto

import data.model.Item
import sim.SimParticipant

open class MeleeMainHand : AutoAttackBase() {
    companion object {
        const val name = "Melee (MH)"
    }

    override val id: Int = 1
    override val name: String = Companion.name

    override fun item(sp: SimParticipant): Item = sp.character.gear.mainHand

    override fun available(sp: SimParticipant): Boolean {
        return sp.hasMainHandWeapon() && super.available(sp)
    }
}
