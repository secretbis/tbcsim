package character.classes.shaman.abilities

import character.Ability
import character.Buff
import character.Mutex
import character.Stats
import character.classes.shaman.talents.MentalQuickness
import character.classes.shaman.talents.TotemicFocus
import data.itemsets.CycloneRegalia
import mechanics.General
import sim.SimParticipant

class WrathOfAirTotem : Ability() {
    companion object {
        const val name = "Wrath of Air Totem"
    }

    override val id: Int = 3738
    override val name: String = Companion.name

    override fun gcdMs(sp: SimParticipant): Int = sp.totemGcd().toInt()

    override fun resourceCost(sp: SimParticipant): Double {
        val tf = sp.character.klass.talents[TotemicFocus.name] as TotemicFocus?
        val tfRed = tf?.totemCostReduction() ?: 0.0

        val mq = sp.character.klass.talents[MentalQuickness.name] as MentalQuickness?
        val mqRed = mq?.instantManaCostReduction() ?: 0.0

        val baseCost = 320.0
        return General.resourceCostReduction(baseCost, listOf(tfRed, mqRed))
    }

    override fun available(sp: SimParticipant): Boolean {
        return true
    }

    val buff = object : Buff() {
        override val name: String = "Wrath of Air Totem"
        override val durationMs: Int = 120000
        override val mutex: List<Mutex> = listOf(Mutex.AIR_TOTEM)

        override fun modifyStats(sp: SimParticipant): Stats {
            // Extra sp from T4 set
            val t4BonusBuff = sp.buffs[CycloneRegalia.TWO_SET_BUFF_NAME] != null
            val t4BonusSpellDamage = if(t4BonusBuff) { CycloneRegalia.twoSetWrathOfAirBonus() } else 0

            return Stats(
                spellDamage = 101 + t4BonusSpellDamage
            )
        }
    }

    override fun cast(sp: SimParticipant) {
        sp.addBuff(buff)
    }
}
