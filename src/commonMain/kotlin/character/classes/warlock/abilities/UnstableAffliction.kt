package character.classes.warlock.abilities

import character.Ability
import character.classes.warlock.debuffs.UnstableAffliction
import sim.SimIteration

class UnstableAffliction : Ability() {
    companion object {
        const val name = "Unstable Affliction"
    }

    override val id: Int = 30405
    override val name: String = Companion.name

    override fun gcdMs(sim: SimIteration): Int = sim.spellGcd().toInt()
    override fun castTimeMs(sim: SimIteration): Int = 1500

    override fun cast(sim: SimIteration) {
        sim.addDebuff(UnstableAffliction())
    }
}
