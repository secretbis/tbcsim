package character.classes.priest.abilities

import character.Ability
import character.classes.priest.buffs.InnerFocus as InnerFocusBuff
import character.classes.priest.buffs.PowerInfusion as PowerInfusionBuff
import character.classes.priest.talents.PowerInfusion as PowerInfusionTalent
import sim.SimParticipant

class PowerInfusion : Ability() {
    companion object {
        const val name: String = "Power Infusion"
    }
    override val id: Int = 10060
    override val name: String = PowerInfusion.name
    override fun gcdMs(sp: SimParticipant): Int = 0
    override fun castTimeMs(sp: SimParticipant): Int = 0
    override fun cooldownMs(sp: SimParticipant): Int = 180000

    override fun resourceCost(sp: SimParticipant): Double {
        val innerFocusBuff = sp.buffs[InnerFocusBuff.name] as InnerFocusBuff?
        if(innerFocusBuff != null){
            sp.consumeBuff(innerFocusBuff)
            return 0.0
        }

        return sp.character.klass.baseMana * 0.16
    }

    override fun available(sp: SimParticipant): Boolean {
        return sp.character.klass.hasTalentRanks(PowerInfusionTalent.name) && super.available(sp)
    }

    override fun cast(sp: SimParticipant) {
        sp.addBuff(PowerInfusionBuff())
    }
}
