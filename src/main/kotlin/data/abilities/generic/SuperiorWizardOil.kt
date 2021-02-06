package data.abilities.generic

import character.Ability
import character.Buff
import character.Stats
import sim.SimIteration

class SuperiorWizardOil : Ability() {
    companion object {
        const val name = "Superior Wizard Oil"
    }

    override val id: Int = 22522
    override val name: String = Companion.name
    override fun gcdMs(sim: SimIteration): Int = 0

    val buff = object : Buff() {
        override val name: String = Companion.name
        override val durationMs: Int = 60 * 60 * 1000
        override val mutex: List<Mutex> = listOf(Mutex.WEAPON_TEMP_ENH_MH)

        override fun modifyStats(sim: SimIteration): Stats {
            return Stats(spellDamage = 42)
        }
    }

    override fun cast(sim: SimIteration) {
        sim.addBuff(buff)
    }
}
