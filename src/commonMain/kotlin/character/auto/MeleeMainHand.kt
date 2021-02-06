package character.auto

import data.model.Item
import sim.SimIteration

class MeleeMainHand : MeleeBase() {
    companion object {
        const val name = "Melee (MH)"
    }

    override val id: Int = 1
    override val name: String = Companion.name

    override fun item(sim: SimIteration): Item = sim.subject.gear.mainHand

    override fun available(sim: SimIteration): Boolean {
        return sim.hasMainHandWeapon() && super.available(sim)
    }
}
