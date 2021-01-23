package character.classes.shaman.abilities

import character.Ability
import sim.SimIteration

class FlameShock(sim: SimIteration) : Ability(sim) {
    override val id: Int = 25457
    override val name: String = "Flame Shock"

    override val baseCastTimeMs: Int = 0
    override val gcdMs: Int = sim.subject.spellGcd().toInt()

    // DoT is 105 Fire damage every 3 seconds for 12 seconds
}
