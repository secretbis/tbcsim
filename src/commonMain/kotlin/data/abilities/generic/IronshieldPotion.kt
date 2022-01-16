package data.abilities.generic

import character.Ability
import character.Buff
import character.Resource
import character.Stats
import sim.SimParticipant
import kotlin.random.Random

class IronshieldPotion : Ability() {
    companion object {
        const val name = "Ironshield Potion"
    }

    override val id: Int = 22832
    override val name: String = Companion.name
    override val icon: String = "inv_potion_133.jpg"
    override fun gcdMs(sp: SimParticipant): Int = 0
    override val castableOnGcd = true
    override val sharedCooldown: SharedCooldown = SharedCooldown.POTION
    override fun cooldownMs(sp: SimParticipant): Int = 120000

    val buff = object : Buff() {
        override val name: String = Companion.name
        override val durationMs: Int = 120000
        override val icon: String = "inv_potion_133.jpg"

        override fun modifyStats(sp: SimParticipant): Stats {
            return Stats(armor = 2500)
        }
    }

    override fun cast(sp: SimParticipant) {
        sp.addBuff(buff)
    }
}
