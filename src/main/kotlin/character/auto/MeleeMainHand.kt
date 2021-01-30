package character.auto

import data.model.Item
import sim.SimIteration

class MeleeMainHand : MeleeBase() {
    override val id: Int = 1
    override val name: String = "Melee (MH)"
    override val isOffhand: Boolean = false

    override fun item(sim: SimIteration): Item = sim.subject.gear.mainHand

    override fun available(sim: SimIteration): Boolean {
        return sim.hasMainHandWeapon() && super.available(sim)
    }
}
