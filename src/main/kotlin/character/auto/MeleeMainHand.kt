package character.auto

import data.model.Item
import sim.Sim

class MeleeMainHand(sim: Sim) : MeleeBase(sim) {
    override val id: Int = 1
    override val name: String = "Melee (MH)"

    override val item: Item
        get() { return sim.subject.gear.mainHand }

    override fun available(): Boolean {
        return sim.subject.hasMainHandWeapon() && super.available()
    }
}
