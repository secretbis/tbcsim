package data.abilities.generic

import character.Ability
import character.Resource
import sim.SimParticipant
import kotlin.random.Random

class DarkRune : Ability() {
    companion object {
        const val name = "Dark Rune"
    }

    override val id: Int = 20520
    override val name: String = Companion.name
    override fun gcdMs(sp: SimParticipant): Int = 0
    override val castableOnGcd = true
    override val sharedCooldown: SharedCooldown = SharedCooldown.RUNE_OR_MANA_GEM
    override fun cooldownMs(sp: SimParticipant): Int = 120000

    override fun cast(sp: SimParticipant) {
        val manaRestored = Random.nextInt(900, 1500)
        sp.addResource(manaRestored, Resource.Type.MANA, name)
    }
}
