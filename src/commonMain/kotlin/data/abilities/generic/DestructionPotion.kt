package data.abilities.generic

import character.Ability
import character.Buff
import character.Mutex
import character.Stats
import mechanics.Rating
import sim.SimParticipant

class DestructionPotion : Ability() {
    companion object {
        const val name = "Destruction Potion"
    }

    override val id: Int = 22839
    override val name: String = Companion.name
    override fun gcdMs(sp: SimParticipant): Int = 0
    override val castableOnGcd = true
    override val sharedCooldown: SharedCooldown = SharedCooldown.POTION
    override fun cooldownMs(sp: SimParticipant): Int = 120000

    val buff = object : Buff() {
        override val name: String = "Destruction Potion"
        override val durationMs: Int = 15000
        override val mutex: List<Mutex> = listOf(Mutex.POTION)

        override fun modifyStats(sp: SimParticipant): Stats {
            return Stats(
                spellDamage = 120,
                spellCritRating = 2.0 * Rating.critPerPct
            )
        }
    }

    override fun cast(sp: SimParticipant) {
        sp.addBuff(buff)
    }
}
