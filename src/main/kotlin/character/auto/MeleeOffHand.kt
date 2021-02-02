package character.auto

import data.model.Item
import sim.SimIteration

class MeleeOffHand : MeleeBase() {
    companion object {
        const val name = "Melee (OH)"
    }

    override val id: Int = 1
    override val name: String = Companion.name
    override val isOffhand: Boolean = true

    override fun item(sim: SimIteration): Item = sim.subject.gear.offHand

    override fun available(sim: SimIteration): Boolean {
        return sim.isDualWielding() && super.available(sim)
    }
}
