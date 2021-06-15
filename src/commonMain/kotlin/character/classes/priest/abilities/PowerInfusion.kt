package character.classes.priest.abilities

import character.Ability
import sim.SimParticipant
import character.classes.priest.buffs.PowerInfusion as PowerInfusionBuff
import character.classes.priest.talents.PowerInfusion as PowerInfusionTalent

class PowerInfusion : Ability() {
    companion object {
        const val name: String = "Power Infusion"
    }
    override val id: Int = 10060
    override val name: String = PowerInfusion.name
    override fun gcdMs(sp: SimParticipant): Int = 0
    override fun castTimeMs(sp: SimParticipant): Int = 0
    override fun cooldownMs(sp: SimParticipant): Int = 180000

    override fun available(sp: SimParticipant): Boolean {
        return sp.character.klass.hasTalentRanks(PowerInfusionTalent.name) && super.available(sp)
    }

    override fun cast(sp: SimParticipant) {
        sp.addBuff(PowerInfusionBuff())
    }
}
