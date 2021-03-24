package character.classes.shaman.abilities

import character.Ability
import character.Buff
import character.Mutex
import character.Stats
import character.classes.shaman.talents.EnhancingTotems
import character.classes.shaman.talents.MentalQuickness
import character.classes.shaman.talents.TotemicFocus
import data.itemsets.CycloneHarness
import mechanics.General
import sim.SimParticipant

class StrengthOfEarthTotem: Ability() {
    companion object {
        const val name = "Strength of Earth Totem"
    }

    override val id: Int = 25528
    override val name: String = Companion.name
    override fun gcdMs(sp: SimParticipant): Int = sp.totemGcd().toInt()

    override fun available(sp: SimParticipant): Boolean {
        return true
    }

    override fun resourceCost(sp: SimParticipant): Double {
        val tf = sp.character.klass.talents[TotemicFocus.name] as TotemicFocus?
        val tfRed = tf?.totemCostReduction() ?: 0.0

        val mq = sp.character.klass.talents[MentalQuickness.name] as MentalQuickness?
        val mqRed = mq?.instantManaCostReduction() ?: 0.0

        return General.resourceCostReduction(300.0, listOf(tfRed, mqRed))
    }

    val buff = object : Buff() {
        override val name: String = "Strength of Earth Totem"
        override val durationMs: Int = 120000
        override val mutex: List<Mutex> = listOf(Mutex.EARTH_TOTEM)

        val baseStr = 86.0
        override fun modifyStats(sp: SimParticipant): Stats {
            // Extra str from T4 set
            val t4BonusBuff = sp.buffs[CycloneHarness.TWO_SET_BUFF_NAME] != null
            val t4BonusStr = if(t4BonusBuff) { CycloneHarness.twoSetStrengthOfEarthBonus() } else 0

            // Enhancing Totems
            val etTalent = sp.character.klass.talents[EnhancingTotems.name] as EnhancingTotems?
            val multiplier = 1.0 * (etTalent?.strengthOfEarthMultiplier() ?: 1.0)

            val totalStr = baseStr + t4BonusStr
            return Stats(strength = (totalStr * multiplier).toInt())
        }
    }

    override fun cast(sp: SimParticipant) {
        sp.addBuff(buff)
    }
}
