package character.classes.shaman.abilities

import character.Ability
import character.Buff
import character.Stats
import sim.SimIteration

class WaterShield : Ability() {
    companion object {
        const val name: String = "Water Shield"
    }
    override val id: Int = 33736
    override val name = Companion.name

    override fun gcdMs(sim: SimIteration): Int = sim.spellGcd().toInt()

    val buff = object : Buff() {
        override val name: String = Companion.name
        override val durationMs: Int = 10 * 60 * 1000

        override fun modifyStats(sim: SimIteration): Stats {
            return Stats(manaPer5Seconds = 50)
        }
    }

    override fun cast(sim: SimIteration, free: Boolean) {
        sim.addBuff(buff)
    }
}
