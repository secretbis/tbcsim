package character.classes.shaman.abilities

import character.Ability
import character.Buff
import character.Stats
import character.classes.shaman.talents.EnhancingTotems
import character.classes.shaman.talents.MentalQuickness
import character.classes.shaman.talents.TotemicFocus
import mechanics.General
import sim.SimIteration

class ManaSpringTotem : Ability() {
    companion object {
        const val name = "Mana Spring Totem"
    }

    override val id: Int = 25570
    override val name: String = Companion.name

    override fun gcdMs(sim: SimIteration): Int = sim.totemGcd().toInt()

    override fun resourceCost(sim: SimIteration): Double {
        val tf = sim.subject.klass.talents[TotemicFocus.name] as TotemicFocus?
        val tfRed = tf?.totemCostReduction() ?: 0.0

        val mq = sim.subject.klass.talents[MentalQuickness.name] as MentalQuickness?
        val mqRed = mq?.instantManaCostReduction() ?: 0.0

        return General.resourceCostReduction(120.0, listOf(tfRed, mqRed))
    }

    override fun available(sim: SimIteration): Boolean {
        return true
    }

    val buff = object : Buff() {
        override val name: String = Companion.name
        override val durationMs: Int = 120000
        override val mutex: List<Mutex> = listOf(Mutex.WATER_TOTEM)

        // TODO: Does anyone care about the Restorative Totems talent?  Probably not

        val baseMp5 = 20.0
        override fun modifyStats(sim: SimIteration): Stats {
            val etTalent = sim.subject.klass.talents[EnhancingTotems.name] as EnhancingTotems?
            val multiplier = 1.0 * (etTalent?.graceOfAirTotemMultiplier() ?: 1.0)
            return Stats(agility = (baseMp5 * multiplier).toInt())
        }
    }

    override fun cast(sim: SimIteration) {
        sim.addBuff(buff)
    }
}
