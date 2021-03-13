package character.classes.shaman.abilities

import character.Ability
import character.Buff
import character.Stats
import sim.SimParticipant

class WaterShield : Ability() {
    companion object {
        const val name: String = "Water Shield"
    }
    override val id: Int = 33736
    override val name = Companion.name

    override fun gcdMs(sp: SimParticipant): Int = sp.spellGcd().toInt()

    val buff = object : Buff() {
        override val name: String = "Water Shield"
        override val durationMs: Int = 10 * 60 * 1000

        override fun modifyStats(sp: SimParticipant): Stats {
            return Stats(manaPer5Seconds = 50)
        }
    }

    override fun cast(sp: SimParticipant) {
        sp.addBuff(buff)
    }
}
