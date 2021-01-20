package character.auto

import data.model.Item
import sim.Sim

class MeleeOffHand(sim: Sim) : MeleeBase(sim) {
    override val id: Int = 1
    override val name: String = "Melee (OH)"

    override val item: Item
        get() { return sim.subject.gear.offHand }

    override fun available(): Boolean {
        return sim.subject.hasOffHandWeapon() && super.available()
    }
}
