package character.classes.hunter.abilities

import character.Ability
import sim.SimParticipant

class HuntersMark : Ability() {
    companion object {
        const val name = "Hunter's Mark"
    }
    override val id: Int
        get() = TODO("Not yet implemented")
    override val name: String = Companion.name
    override fun gcdMs(sp: SimParticipant): Int = sp.physicalGcd().toInt()

    override fun cast(sp: SimParticipant) {
        TODO("Not yet implemented")
    }
}
