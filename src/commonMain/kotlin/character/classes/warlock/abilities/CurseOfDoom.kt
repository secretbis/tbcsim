package character.classes.warlock.abilities

import character.Ability
import character.classes.warlock.debuffs.CurseOfDoom
import sim.SimIteration

class CurseOfDoom : Ability() {
    companion object {
        const val name = "Curse of Doom"
    }

    override val id: Int = 27216
    override val name: String = Companion.name

    override fun gcdMs(sim: SimIteration): Int = sim.spellGcd().toInt()

    override fun cast(sim: SimIteration) {
        sim.addDebuff(CurseOfDoom())
    }
}
