package character.classes.warlock.abilities

import character.Ability
import sim.SimParticipant

class Nightfall : Ability() {
    companion object {
        const val name = "Nightfall"
        const val icon = "spell_shadow_twilight.jpg"
    }

    override val id: Int = 18095
    override val name: String = Companion.name
    override val icon: String = Companion.icon

    override fun gcdMs(sp: SimParticipant): Int = 0
    override fun cast(sp: SimParticipant) {
        // Noop
    }
}
