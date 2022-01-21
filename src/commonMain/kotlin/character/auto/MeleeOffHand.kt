package character.auto

import data.model.Item
import sim.SimParticipant

open class MeleeOffHand(fireIncomingEvents: Boolean = false) : AutoAttackBase(fireIncomingEvents) {
    companion object {
        const val name = "Melee (OH)"
    }

    override val id: Int = 1
    override val name: String = Companion.name

    override fun item(sp: SimParticipant): Item = sp.character.gear.offHand

    override fun available(sp: SimParticipant): Boolean {
        return sp.isDualWielding() && super.available(sp)
    }
}
