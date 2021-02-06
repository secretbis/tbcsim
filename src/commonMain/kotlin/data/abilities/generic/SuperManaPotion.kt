package data.abilities.generic

import character.Ability
import character.Resource
import sim.SimIteration
import kotlin.random.Random

class SuperManaPotion : Ability() {
    companion object {
        const val name = "Super Mana Potion"
    }

    override val id: Int = 22832
    override val name: String = Companion.name
    override fun gcdMs(sim: SimIteration): Int = 0
    override val castableOnGcd = true
    override val sharedCooldown: SharedCooldown = SharedCooldown.POTION
    override fun cooldownMs(sim: SimIteration): Int = 120000

    override fun cast(sim: SimIteration) {
        val manaRestored = Random.nextInt(1800, 3000)
        sim.addResource(manaRestored, Resource.Type.MANA)
    }
}
