package character.classes.shaman.abilities

import character.Ability
import character.Buff
import character.Proc
import character.Stats
import character.classes.shaman.talents.EnhancingTotems
import character.classes.shaman.talents.TotemicFocus
import sim.SimIteration

class StrengthOfEarthTotem: Ability() {
    companion object {
        const val name = "Strength of Earth Totem"
    }

    override val id: Int = 25528
    override val name: String = Companion.name

    override fun available(sim: SimIteration): Boolean {
        return true
    }

    override fun resourceCost(sim: SimIteration): Double {
        // TODO: Does this count as an "instant spell" for mental quickness?
        val tf = sim.subject.klass.talents[TotemicFocus.name] as TotemicFocus?
        val tfMult = tf?.totemCostMultiplier() ?: 1.0
        return 300.0 * tfMult
    }

    val buff = object : Buff() {
        override val name: String = Companion.name
        override val durationMs: Int = 120000
        override val mutex: List<Mutex> = listOf(Mutex.EARTH_TOTEM)

        val baseStr = 86.0
        override fun modifyStats(sim: SimIteration): Stats {
            val etTalent = sim.subject.klass.talents[EnhancingTotems.name] as EnhancingTotems?
            val multiplier = 1.0 * (etTalent?.strengthOfEarthMultiplier() ?: 1.0)
            return Stats(strength = (baseStr * multiplier).toInt())
        }
    }

    override fun cast(sim: SimIteration, free: Boolean) {
        sim.addBuff(buff)
    }

    override val baseCastTimeMs: Int = 0
    override fun gcdMs(sim: SimIteration): Int = sim.totemGcd().toInt()
}
