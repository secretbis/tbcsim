package character.classes.shaman.abilities

import character.Ability
import sim.SimIteration

class EarthShock(sim: SimIteration) : Ability(sim) {
    override val id: Int = 25454
    override val name: String = "Earth Shock"

    override val baseCastTimeMs: Int = 0
    override val gcdMs: Int = sim.subject.spellGcd().toInt()

    val baseDamage = Pair(658, 693)
    override fun cast(free: Boolean) {
        super.cast(free)


    }
}
