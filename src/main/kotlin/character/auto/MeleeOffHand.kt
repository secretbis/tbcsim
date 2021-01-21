package character.auto

import data.model.Item
import sim.SimIteration

class MeleeOffHand(sim: SimIteration) : MeleeBase(sim) {
    override val id: Int = 1
    override val name: String = "Melee (OH)"
    override val isOffhand: Boolean = true

    override val item: Item
        get() { return sim.subject.gear.offHand }

    override fun available(): Boolean {
        return sim.subject.hasOffHandWeapon() && super.available()
    }
}
