package character.classes.warlock.abilities

import character.Ability
import character.classes.warlock.debuffs.Corruption
import sim.SimIteration

class SiphonLife : Ability() {
    companion object {
        const val name = "Siphon Life"
    }

    override val id: Int = 30911
    override val name: String = Companion.name

    override fun gcdMs(sim: SimIteration): Int = sim.spellGcd().toInt()

    override fun cast(sim: SimIteration) {
        sim.addDebuff(Corruption())
    }
}
