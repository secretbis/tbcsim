package character.classes.priest.abilities

import character.classes.priest.buffs.PowerInfusion
import character.classes.priest.talents.*
import character.Ability
import character.Buff
import data.itemsets.IncarnateRegalia
import sim.SimParticipant

class Shadowfiend : Ability() {
    companion object {
        const val name: String = "Shadowfiend"
    }
    override val id: Int = 34433
    override val name: String = Companion.name
    override fun gcdMs(sp: SimParticipant): Int = sp.spellGcd().toInt()
    override fun castTimeMs(sp: SimParticipant): Int  = 0

    override fun cooldownMs(sp: SimParticipant): Int = 300000

    override fun resourceCost(sp: SimParticipant): Double {
        val mentalAgility = sp.character.klass.talents[MentalAgility.name] as MentalAgility?
        val mentalAgilityManaCostMultiplier = mentalAgility?.instantSpellManaCostReductionMultiplier() ?: 0.0

        val piBuff = sp.buffs[PowerInfusion.name] as PowerInfusion?
        val piMult = piBuff?.manaCostMultiplier() ?: 1.0

        return sp.character.klass.baseMana * 0.06 * mentalAgilityManaCostMultiplier * piMult
    }

    val buff = object : Buff() {
        override val name: String = Companion.name
        override val durationMs: Int = 15000
        override val hidden: Boolean = true

        override fun reset(sp: SimParticipant) {
            sp.pet?.deactivate(true)
            super.reset(sp)
        }
    }

    val t4Buff = object : Buff() {
        override val name: String = Companion.name
        override val durationMs: Int = 18000
        override val hidden: Boolean = true

        override fun reset(sp: SimParticipant) {
            sp.pet?.deactivate(true)
            super.reset(sp)
        }
    }

    override fun cast(sp: SimParticipant) {        
        sp.pet?.activate()

        val t4Bonus = sp.buffs[IncarnateRegalia.TWO_SET_BUFF_NAME] != null
        if(t4Bonus) {
            sp.addBuff(t4Buff)
        } else {
           sp.addBuff(buff)
        }        
    }
}
