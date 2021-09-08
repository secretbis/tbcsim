package character.classes.priest.abilities

import character.classes.priest.buffs.InnerFocus as InnerFocusBuff
import character.classes.priest.talents.*
import character.Ability
import character.Buff
import data.itemsets.IncarnateRegalia
import sim.SimParticipant

class Shadowfiend() : Ability() {
    companion object {
        const val name: String = "Shadowfiend"
    }

    override val id: Int = 34433
    override val name: String = Companion.name

    val baseDurationMs = 15000

    fun fiendUpBuff(sp: SimParticipant) = object : Buff() {
        override val name: String = "${Companion.name} (Hidden)"
        override val hidden: Boolean = true
        override val durationMs: Int = if(sp.buffs[IncarnateRegalia.TWO_SET_BUFF_NAME] != null) {
            baseDurationMs + 3000
        } else baseDurationMs
        

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

        sp.addBuff(fiendUpBuff(sp))    
    }
}
