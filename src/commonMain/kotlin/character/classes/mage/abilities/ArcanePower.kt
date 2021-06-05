package character.classes.mage.abilities

import character.Ability
import sim.SimParticipant
import character.classes.mage.buffs.ArcanePower as ArcanePowerBuff
import character.classes.mage.talents.ArcanePower as ArcanePowerTalent

class ArcanePower : Ability() {
    companion object {
        const val name: String = "Arcane Power"
    }
    override val id: Int = 12042
    override val name: String = ArcanePower.name
    override fun gcdMs(sp: SimParticipant): Int = 0
    override fun castTimeMs(sp: SimParticipant): Int = 0
    override fun cooldownMs(sp: SimParticipant): Int = 180000

    override fun available(sp: SimParticipant): Boolean {
        return sp.character.klass.hasTalentRanks(ArcanePowerTalent.name) && super.available(sp)
    }

    override fun cast(sp: SimParticipant) {
        sp.addBuff(ArcanePowerBuff())
    }
}
