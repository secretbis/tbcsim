package data.abilities.generic

import character.*
import sim.SimParticipant

class FlameCap : Ability() {
    companion object {
        const val name = "Flame Cap"
        const val icon: String = "inv_misc_herb_flamecap.jpg"
    }

    override val id: Int = 22788
    override val name: String = Companion.name
    override val icon: String = Companion.icon
    override fun gcdMs(sp: SimParticipant): Int = 0
    override val castableOnGcd = true
    override val sharedCooldown: SharedCooldown = SharedCooldown.RUNE_OR_MANA_GEM
    override fun cooldownMs(sp: SimParticipant): Int = 180000

    val buff = object : Buff() {
        override val name: String = Companion.name
        override val icon: String = Companion.icon
        override val durationMs: Int = 60000

        override fun modifyStats(sp: SimParticipant): Stats {
            return Stats(fireDamage = 80)
        }
    }

    override fun cast(sp: SimParticipant) {
        sp.addBuff(buff)
    }
}
