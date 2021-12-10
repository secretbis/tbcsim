package character.classes.hunter.abilities

import character.Ability
import sim.SimParticipant

class Readiness : Ability() {
    companion object {
        const val name = "Readiness"
        const val icon = "ability_hunter_readiness.jpg"
    }
    override val id: Int = 23989
    override val name: String = Companion.name
    override val icon: String = Companion.icon
    override fun gcdMs(sp: SimParticipant): Int = 0
    override fun cooldownMs(sp: SimParticipant): Int = 300000
    override fun resourceCost(sp: SimParticipant): Double = 0.0

    override fun cast(sp: SimParticipant) {
        sp.abilityState[ArcaneShot.name]?.cooldownStartMs = -1
        sp.abilityState[KillCommand.name]?.cooldownStartMs = -1
        sp.abilityState[MultiShot.name]?.cooldownStartMs = -1
        sp.abilityState[RapidFire.name]?.cooldownStartMs = -1
    }
}
