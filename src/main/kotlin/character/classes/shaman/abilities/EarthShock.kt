package character.classes.shaman.abilities

import character.Ability
import sim.SimIteration

class EarthShock(sim: SimIteration) : Ability(sim) {
    override val id: Int = 25457
    override val name: String = "Earth Shock"

    override fun castTimeMs(): Int = 0

    override fun gcdMs(): Int {
        return sim.subject.spellGcd().toInt()
    }
}
