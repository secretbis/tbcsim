package data.abilities.generic

import character.Ability
import character.Resource
import sim.SimParticipant
import kotlin.random.Random

class SuperManaPotion : Ability() {
    companion object {
        const val name = "Super Mana Potion"
    }

    override val id: Int = 22832
    override val name: String = Companion.name
    override fun gcdMs(sp: SimParticipant): Int = 0
    override val castableOnGcd = true
    override val sharedCooldown: SharedCooldown = SharedCooldown.POTION
    override fun cooldownMs(sp: SimParticipant): Int = 120000

    override fun cast(sp: SimParticipant) {
        val manaRestored = Random.nextInt(1800, 3000)
        sp.addResource(manaRestored, Resource.Type.MANA, name)
    }
}
