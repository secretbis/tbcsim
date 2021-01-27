package character.auto

import data.model.Item
import sim.SimIteration

class MeleeOffHand : MeleeBase() {
    override val id: Int = 1
    override val name: String = "Melee (OH)"
    override val isOffhand: Boolean = true

    override fun item(sim: SimIteration): Item = sim.subject.gear.offHand

    override fun available(sim: SimIteration): Boolean {
        return sim.subject.isDualWielding() && super.available(sim)
    }
}
