package character.classes.mage.abilities

import character.Ability
import character.classes.mage.talents.IceFloes
import sim.SimParticipant

class ColdSnap : Ability() {
    companion object {
        const val name: String = "Cold Snap"
    }
    override val id: Int = 11958
    override val name: String = Companion.name
    override fun gcdMs(sp: SimParticipant): Int = 0
    override fun castTimeMs(sp: SimParticipant): Int = 0

    val baseCooldownMs = 480000
    override fun cooldownMs(sp: SimParticipant): Int {
        val iceFloesMult = 1.0 - (sp.character.klass.talentRanks(IceFloes.name) * 0.1)
        return (baseCooldownMs * iceFloesMult).toInt()
    }

    override fun cast(sp: SimParticipant) {
        sp.abilityState[IcyVeins.name]?.cooldownStartMs = -1
        sp.abilityState[SummonWaterElemental.name]?.cooldownStartMs = -1
    }
}
