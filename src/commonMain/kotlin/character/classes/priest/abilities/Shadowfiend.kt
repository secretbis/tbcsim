package character.classes.priest.abilities

import character.classes.priest.buffs.InnerFocus as InnerFocusBuff
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

    val fiendUpBuff = object : Buff() {
        override val name: String = "${Shadowfiend.name} (Hidden)"
        override val durationMs: Int = 15000
        override val hidden: Boolean = true

        override fun reset(sp: SimParticipant) {
            sp.pet?.deactivate(true)
            super.reset(sp)
        }
    }

    override fun gcdMs(sp: SimParticipant): Int = sp.spellGcd().toInt()

    override fun cooldownMs(sp: SimParticipant): Int = 300000

    override fun resourceCost(sp: SimParticipant): Double {
        val innerFocusBuff = sp.buffs[InnerFocusBuff.name] as InnerFocusBuff?

        if(innerFocusBuff != null){
            return 0.0
        }

        return sp.character.klass.baseMana * 0.06
    }

    override fun cast(sp: SimParticipant) {    
        sp.pet?.activate()

        sp.addBuff(fiendUpBuff)    
    }
}
