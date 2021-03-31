package character.classes.hunter.abilities

import character.Ability
import character.Buff
import character.Stats
import character.classes.hunter.talents.RapidKilling
import mechanics.Rating
import sim.SimParticipant

class RapidFire : Ability() {
    companion object {
        const val name = "Rapid Fire"
    }
    override val id: Int = 3045
    override val name: String = Companion.name
    override fun gcdMs(sp: SimParticipant): Int = 0
    override fun cooldownMs(sp: SimParticipant): Int {
        val rk = sp.character.klass.talents[RapidKilling.name] as RapidKilling?
        val cdReduction = rk?.rapidFireCooldownReductionMs() ?: 0
        return 300000 - cdReduction
    }
    override fun resourceCost(sp: SimParticipant): Double = 100.0

    val buff = object : Buff() {
        override val name: String = Companion.name
        override val durationMs: Int = 15000

        override fun modifyStats(sp: SimParticipant): Stats {
            return Stats(physicalHasteRating = 40.0 * Rating.hastePerPct)
        }
    }

    override fun cast(sp: SimParticipant) {
        sp.addBuff(buff)
    }
}
