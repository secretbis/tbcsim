package character.classes.shaman.abilities

import character.Ability
import character.Buff
import character.Stats
import character.classes.shaman.talents.EnhancingTotems
import character.classes.shaman.talents.MentalQuickness
import character.classes.shaman.talents.TotemicFocus
import mechanics.General
import sim.SimIteration

class GraceOfAirTotem : Ability() {
    companion object {
        const val name = "Grace of Air Totem"
    }

    override val id: Int = 25359
    override val name: String = Companion.name

    override fun gcdMs(sim: SimIteration): Int = sim.totemGcd().toInt()

    override fun resourceCost(sim: SimIteration): Double {
        val tf = sim.subject.klass.talents[TotemicFocus.name] as TotemicFocus?
        val tfRed = tf?.totemCostReduction() ?: 0.0

        val mq = sim.subject.klass.talents[MentalQuickness.name] as MentalQuickness?
        val mqRed = mq?.instantManaCostReduction() ?: 0.0

        return General.resourceCostReduction(310.0, listOf(tfRed, mqRed))
    }

    override fun available(sim: SimIteration): Boolean {
        return true
    }

    val buff = object : Buff() {
        override val name: String = Companion.name
        override val durationMs: Int = 120000
        override val mutex: List<Mutex> = listOf(Mutex.AIR_TOTEM)

        val baseAgi = 77.0
        override fun modifyStats(sim: SimIteration): Stats {
            val etTalent = sim.subject.klass.talents[EnhancingTotems.name] as EnhancingTotems?
            val multiplier = 1.0 * (etTalent?.graceOfAirTotemMultiplier() ?: 1.0)
            return Stats(agility = (baseAgi * multiplier).toInt())
        }
    }

    override fun cast(sim: SimIteration, free: Boolean) {
        sim.addBuff(buff)
    }
}
